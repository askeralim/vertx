import React, { Component } from "react";
import EventBus from "vertx3-eventbus-client";
import fetch from "node-fetch";
import { Link } from "react-router-dom";

class Register extends Component {
  constructor() {
    super();
    this.state = {
      registered: false,
      status: "",
      nickName: "",
      username: "",
      password: ""
    };
    this.handleNickNameChange = this.handleNickNameChange.bind(this);
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.register = this.register.bind(this);
  }

  register(event) {
    let message = false;
    if(this.state.nickName.length<5){
      message="Nick name should be minimum 5 characters";
    }else if(this.state.username.length<5){
      message="Username should be minimum 5 characters";
    }else if(this.state.password.length<5){
      message="Password must be of minimum length 5 characters";
    }
    if(message){
      this.setState({
        status: (
          <div class="alert alert-danger">
           {message}
          </div>
        )
      });
      return;
    }

    const url = window.location.origin + "/user/register";
    var headers = {
      "Content-Type": "application/json"
    };
    let userData = {
      nickName: this.state.nickName,
      username: this.state.username,
      password: this.state.password
    };
    fetch(url, {
      method: "POST",
      headers: headers,
      body: JSON.stringify(userData)
    }) //{'username':'Asker','password':'Password'})})
      .then(res => res.json())
      .then(res => {
        if (res.status == "Success") {
          console.log(
            JSON.stringify(res) + "===== REGISTER SUCCESS =====" + res.status
          );
          this.setState({
            registered: true,
            nickName: "",
            username: "",
            password: "",
            status: (
              <div class="alert alert-success">
                <strong>Registration Successful!</strong> Please Login with
                credentials.
              </div>
            )
          });
        } else {
          console.log(
            JSON.stringify(res) + "===== REGISTER FAIL =====" + res.status
          );
          this.setState({
            registered: true,
            status: (
              <div class="alert alert-danger">
                <strong>Registration Failed!</strong> Please try later.
              </div>
            )
          });
        }
      })
      .catch(error => {
        console.log("Error in Request ");
        this.setState({
          registered: true,
          status: "Registration Failed, Please try later"
        });
      });
  }
  handleNickNameChange(event) {
    this.setState({ nickName: event.target.value });
  }
  handleUsernameChange(event) {
    this.setState({ username: event.target.value });
  }
  handlePasswordChange(event) {
    this.setState({ password: event.target.value });
  }

  render() {
    return (
      <div class="login-page">
        <div class="form">
          <form class="register-form">
            {this.state.status}
            <input
              type="text"
              placeholder="nickname"
              value={this.state.nickName}
              onChange={this.handleNickNameChange}
            />
            <input
              type="text"
              placeholder="username"
              value={this.state.username}
              onChange={this.handleUsernameChange}
            />
            <input
              type="password"
              placeholder="password"
              value={this.state.password}
              onChange={this.handlePasswordChange}
            />
            <button type="button" onClick={this.register}>
              create
            </button>
            <p class="message">
              Already registered? <Link to="/login">Sign In</Link>
            </p>
          </form>
        </div>
      </div>
    );
  }
}

export default Register;

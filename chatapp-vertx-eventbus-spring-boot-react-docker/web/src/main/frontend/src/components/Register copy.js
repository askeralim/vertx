import React, { Component } from "react";
import EventBus from "vertx3-eventbus-client";
import fetch from 'node-fetch';
import { Link } from "react-router-dom";

class Register extends Component {
  constructor() {
    super();
    this.handleChange = this.handleChange.bind(this);
  }

  register(event) {
    console.log('Login Method called :'+this.state.userName+"   "+this.state.password);
    this.props.executeLoginAction({username:this.state.userName, password:this.state.password});
  }
  handleNickrNameChange(event) {
    this.setState({nickName: event.target.value, password:this.state.password});
  }
  handleUserNameChange(event) {
    this.setState({userName: event.target.value, password:this.state.password});
  }
  handlePasswordChange(event) {
    this.setState({userName:this.state.userName,password: event.target.value});
  }
  componentDidMount() {
   // $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
    let data = this.state.data;
    this.eventBus = new EventBus(window.location.href+"/eventbus");
    const eb = this.eventBus;
    const self = this;
    // Listen for messages coming in
    eb.onopen = function() {
      eb.registerHandler("chat.message", (err, message) => {
        if (!err) {
          let msg = JSON.stringify(message);
          console.log("Received Message :" + msg);
          msg = msg.replace('"{', "{").replace('}"', "}");
          console.log("Parsed - Message :" + msg);
          msg = msg.replace(/'/g, '"');
          console.log("Parsed - Message :" + msg);
          msg = JSON.parse(msg);
          data = self.state.data;
          const messages = data.messages;
          messages.push(msg.body);
          self.setState({
            data: {
              message: self.state.message,
              messages
            }
          });
        } else {
          console.log("Error");
        }
      });
    };
  }

  render() {
    return (
      <div class="login-page">
      <div class="form">
          <form class="register-form">
            <input type="text" placeholder="nickname" value={this.state.nickName} onChange={this.handleNickNameChange}/>
            <input type="text" placeholder="username" value={this.state.userName} onChange={this.handleUserNameChange}/>
            <input type="password" placeholder="password" value={this.state.password} onChange={this.handlePasswordChange}/>
            <button type='button' onClick={this.register}>create</button>
            <p class="message">
              Already registered?  <Link to="/login">Sign In</Link>
            </p>
          </form>
        </div>
      </div>
    );
  }
}
export default Register;
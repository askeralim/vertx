import React, { Component } from "react";
import { Link, Redirect } from "react-router-dom";
import { executeLoginAction } from '../actions/index';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import Home from "./Home";
class Login extends Component {
  constructor(props) {
    super(props);
    this.state={userName:'',password:''};
    this.handleUserNameChange = this.handleUserNameChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.login = this.login.bind(this);
  }

  handleUserNameChange(event) {
    this.setState({userName: event.target.value});
  }
  handlePasswordChange(event) {
    this.setState({password: event.target.value});
  }
  login(event) {
    console.log('Login Method called :'+this.state.userName+"   "+this.state.password);
    this.props.executeLoginAction({username:this.state.userName, password:this.state.password});
  }

  render() {
    if (this.props.loginToken && !this.props.loginToken.message && this.props.loginToken.get('status') == 'Success') return <Redirect to='/' />;
    let error ="";
    if (this.props.loginToken && !this.props.loginToken.message  && this.props.loginToken.get('status') == 'Failed')
        error =  <div class="alert alert-danger">
                <strong>Login Unsuccessful!</strong> Please check the credentials.
              </div>
    else if (this.props.loginToken && this.props.loginToken.message )
    error =  <div class="alert alert-danger">
            <strong>Session expired</strong> Please login again.
          </div>
    return (
      <div class="login-page">
        <div class="form">
          <form class="login-form">
          {error}
            <input type="text" placeholder="username" value={this.state.userName} onChange={this.handleUserNameChange} />
            <input type="password" placeholder="password" value={this.state.password} onChange={this.handlePasswordChange}/>
            <button type='button' onClick={this.login}>login</button>
            <p class="message">
              Not registered? <Link to="/register">Create an account</Link>
            </p>
          </form>
        </div>
      </div>
    );
  }
}
//export default Login;
function mapStateToProps(state){
  return {
    loginToken : state.loginToken
  }
}
function mapDispatchToProps(dispath){
  return bindActionCreators({executeLoginAction}, dispath);
}

export default connect(mapStateToProps, mapDispatchToProps)(Login);

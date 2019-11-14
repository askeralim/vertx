import React, { Component } from 'react';
import { connect } from 'react-redux';
import SearchBar from '../container/searchbar.js'
import ChatRoomList from '../container/chatroom-list.js'
import MessagePanel from '../container/message-panel.js'
import { Redirect } from "react-router-dom";
//import socket from "./socket.js"
// import eventBus from './eventbus'
import EventBus from "vertx3-eventbus-client";
import Dialog from 'react-bootstrap-dialog'
import { createChatRoomAction, logOffFromChatServer, newChatRoomAction, newMessageAction, usersListAction, myChatRoomList, loginError } from '../actions/index';
import fetch from "node-fetch";

class Home extends Component {
  constructor(props) {
    super(props);
   // this.state = { modalIsOpen: false };
    this.openCreateChatRoomModal = this.openCreateChatRoomModal.bind(this)
    this.handleWindowClose = this.handleWindowClose.bind(this)
    console.log("Login Token in COnstructor: "+this.props.loginToken);
  }
  componentDidMount() {
    console.log('Logged In User :'+JSON.stringify(this.props.loginToken));
   // this.props.history.push("/login");
    window.addEventListener('onbeforeunload', this.handleWindowClose);
    this.loadUserDetails(this.props.dispatch);
    const self = this;
    const dispatch = this.props.dispatch;
    this.eventBus = new EventBus(window.location.origin+"/eventbus");
    const eb = this.eventBus;//this.eventBus;

    const loadUserDetails = this.loadUserDetails;
    // Listen for messages coming in
    eb.onopen = function() {
      console.log("Home.js EventBus State:"+eb.state)
      eb.registerHandler("chat.room", (err, message) => {
        if (!err) {
          dispatch(newChatRoomAction(JSON.parse(message.body)));
        } else {
          console.log("Error");
        }
      });
      eb.registerHandler("chat.message", (err, message) => {//"chat."+self.props.loginToken.get('id')
        if (!err) {
          dispatch(newMessageAction(message));
          console.log("Asker Message :"+message);
        } else {
          console.log("Asker :Error");
        }
      });
      //loadUserDetails(dispatch, eb);
    };
  }
  loadUserDetails(dispatch){
    let loginToken = JSON.parse(localStorage.getItem("loginToken"));
    const url = window.location.origin + "/user/login";
    var headers = {
      "Content-Type": "application/json",
      "token":loginToken.token
    };
    fetch("/user/list", {
      method: "GET",
      headers: headers
    })
      .then(res => {
        if(res.status!==200){
          throw new Error(res.status)
        }
        return res.json();
      })
      .then(res => {
        console.log("Received User List" + JSON.stringify(res));
        dispatch(
          usersListAction({ me: loginToken.id, userList: res })
        );
        fetch("/chatroom/list", {method: "GET", headers: headers}).then(res => res.json()).then(res =>  {
          //TODO Register in Event Bus
          dispatch(myChatRoomList(res));
          res.forEach(room=>{
            console.log(room.id.groupId);
            //console.log("ChatList.js EventBus State:"+eb.state)
            //chatRoomList = chatRoomList.map(room => room.get('id'));
            // eb.registerHandler("chat."+room.id.groupId, (err, message) => {
            //   if (!err) {
            //     dispatch(newMessageAction(message));
            //     console.log("Group Message Message :"+message);
            //   } else {
            //     console.log("Group Message :Error");
            //   }
            // });
          });
        });
      })
      .catch(error => {
        console.log('Error in Request ');
        dispatch(loginError(error));
      });
  }
  componentWillUnmount() {
     //console.log('Unmounting APP Component ::::::::::::::');
     window.removeEventListener('onbeforeunload', this.handleWindowClose);
  }
  handleWindowClose(){
   // this.props.dispatch(logOffFromChatServer(socket));
  }
  openCreateChatRoomModal() {
   this.setState({modalIsOpen: true});
   this.dialog.show({
     body: 'Chat room Name Please..',
     prompt: Dialog.TextPrompt({initialValue: 'Weekend Hangout',placeholder: 'Chat room Name'}),
     actions: [
       Dialog.OKAction((dialog) => {
         const result = dialog.value
         if(result && result.length >0){
           this.setState({modalIsOpen: false});
         }
         let loginToken = JSON.parse(localStorage.getItem('loginToken'));
        
         this.props.dispatch(createChatRoomAction(loginToken.token,result));
       }),
       Dialog.CancelAction(() => {
        this.setState({modalIsOpen: false});
        console.log('Cancel was clicked!')
      })
     ],
     onHide: (dialog) => {  }
   })
 }
  render() {
    if(this.props.loginToken.message == 401){
      localStorage.removeItem('loginToken');//UnAuthorised Token, Moving to Login again
      return <Redirect to='/' />;
    }else 
    return (
      <div className="row app-one">
        <div className="col-sm-4 side">
          <div className="side-one">
            <div className="row heading">
            <div className="col-sm-6 col-xs-6 heading-avatar">
                <div className="heading-avatar-icon">
                  <a className="heading-name-meta">{this.props.loginToken.get('name')}</a>
                </div>
              </div>
              <div className="col-sm-2 col-xs-2 heading-compose  pull-right" onClick={this.openCreateChatRoomModal}>
                <i className="fa fa-comments fa-2x  pull-right" aria-hidden="true"></i>
              </div>
            </div>
            <div className="row searchBox">
              <SearchBar />
            </div>
            <div className="row sideBar">
              <ChatRoomList />
            </div>
          </div>
        </div>
        <MessagePanel />
        <Dialog ref={(el) => { this.dialog = el }} />
      </div>
    );
  }
}

function mapStateToProps({loginToken}){
  return {loginToken}
}
export default connect(mapStateToProps)(Home);

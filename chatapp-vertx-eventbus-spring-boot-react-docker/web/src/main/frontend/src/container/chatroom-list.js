import React, { Component } from "react";
import { connect } from "react-redux";
import {
  CHAT_PERSON_TYPE_CHAT_ROOM,
  CHAT_PERSON_TYPE_USER,
  selectChatRoomDispatcher,
  joinChatRoom,
  usersListAction,
  myChatRoomList,
  newMessageAction,
  loginError
} from "../actions/index";
import Dialog from "react-bootstrap-dialog";
import fetch from "node-fetch";

class ChatRoomList extends Component {
  constructor(props) {
    super(props);
  }
  componentDidMount() {
    console.log("Component Mounted : Looking for Nick Name");
  }
  renderImage(chatRoom) {
    if (chatRoom.get("type") == CHAT_PERSON_TYPE_USER)
      return (
        <div className="avatar-icon">
          <img src="img/man.png" />
        </div>
      );
    else
      return (
        <div className="avatar-icon">
          <img src="img/group.png" />
        </div>
      );
  }
  renderChatName(chatRoom) {
    if (chatRoom.get("newMessage"))
      return (
        <span className="name-meta">
          <strong>{chatRoom.get("nickName")}</strong>
        </span>
      );
    else return <span className="name-meta">{chatRoom.get("nickName")}</span>;
  }
  renderJoinButton(chatRoom) {
    if (
      chatRoom.get("type") == CHAT_PERSON_TYPE_CHAT_ROOM &&
      chatRoom.get("joined")
    )
      return (
        <div className="col-sm-4 col-xs-4 pull-left sideBar-time">
          <span className="name-meta"></span>
        </div>
      );
    else if (chatRoom.get("type") == CHAT_PERSON_TYPE_CHAT_ROOM)
      return (
        <div className="col-sm-4 col-xs-4 pull-left sideBar-time">
          <button type="button" onClick={() => {
              let loginToken = JSON.parse(localStorage.getItem('loginToken'));
              this.props.dispatch(joinChatRoom(loginToken.token, chatRoom.get('id')));
            }}>
            Join
          </button>
        </div>
      );
    else
      return (
        <div className="col-sm-4 col-xs-4 pull-right sideBar-time">
        </div>
      );
  }
  renderList() {
    if (!this.props.chatRoomList) return <div />;
    let chatRoomList = this.props.chatRoomList.filter(
      room =>
        room.get("id") != this.props.loggedInUser.get("id") &&
        room.get("show") == true
    );
    return chatRoomList.map(chatRoom => {
      return (
        <div
          className="row sideBar-body"
          key={chatRoom.get("id")}
          onClick={() =>{
            if((chatRoom.get("type") == CHAT_PERSON_TYPE_CHAT_ROOM && chatRoom.get("joined")) || chatRoom.get("type") == CHAT_PERSON_TYPE_USER)
              this.props.dispatch(selectChatRoomDispatcher(chatRoom))
            }
          }
        >
          <div className="col-sm-3 col-xs-3 sideBar-avatar">
            {this.renderImage(chatRoom)}
          </div>
          <div className="col-sm-9 col-xs-9 sideBar-main">
            <div className="row">
              <div className="col-sm-8 col-xs-8 sideBar-name">
                {this.renderChatName(chatRoom)}
              </div>
              <div className="col-sm-4 col-xs-4 pull-right sideBar-time">
                <span className="time-meta pull-right">
                  {chatRoom.get("time")}
                </span>
                {this.renderJoinButton(chatRoom)}
              </div>
            </div>
          </div>
        </div>
      );
    });
  }
  render() {
    return (
      <div className="row sideBar">
        <Dialog
          ref={el => {
            this.dialog = el;
          }}
        />
        {this.renderList()}
      </div>
    );
  }
}
const mapDispatchToProps = dispatch => {
  return {
    chatRoomOnClick: chatRoom => dispatch(selectChatRoomDispatcher(chatRoom))
  };
};

function mapStateToProps({ chatRoomList, loggedInUser }) {
  return { chatRoomList, loggedInUser }; 
}
/*function mapDispatchToProps(dispatch){
  return bindActionCreators({selectChatRoom : selectChatRoom}, dispatch);
}*/

export default connect(mapStateToProps)(ChatRoomList);

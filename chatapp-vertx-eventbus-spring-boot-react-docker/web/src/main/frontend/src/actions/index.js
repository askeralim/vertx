import fetch from 'node-fetch';
import { BrowserRouter, Route,  } from "react-router-dom";
export const SEARCH_CHATROOM = "SEARCH_CHATROOM";
export const CHAT_ROOM_SELECTED = "CHAT_ROOM_SELECTED";
export const SEND_MESSAGE = "SEND_MESSAGE";
export const CHAT_ROOM_LIST = "CHAT_ROOM_LIST";
export const USER_LIST = "USER_LIST";
export const CHAT_SERVER_LOGIN = "CHAT_LOGIN";
export const LOGGED_IN_USER = "LOGGED_IN_USER";
export const NEW_USER = "NEW_USER";
export const NEW_CHAT_ROOM = "NEW_CHAT_ROOM";
export const NEW_MESSAGE = "NEW_MESSAGE";
export const CHAT_PERSON_TYPE_USER = "USER";

export const CHAT_PERSON_TYPE_CHAT_ROOM = "CHAT_ROOM";
export const CURRENT_CHAT_ROOM_LIST = "CURRENT_CHAT_ROOM_LIST";
export const JOINED_A_CHATROOM = "JOINED_A_CHATROOM";
export const MY_CHAT_ROOM_LIST = "MY_CHAT_ROOM_LIST";

export const EXECUTE_LOGIN = "EXECUTE_LOGIN";
export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGIN_FAIL = "LOGIN_FAIL";

export const executeLoginAction = (credentials) => {
  return dispatch => {
    console.log('Posting Login to Server');
    const url = window.location.origin+'/user/login';
    var headers = {
        "Content-Type": "application/json"
      }
      console.log(credentials);
      console.log(JSON.stringify(credentials));
    fetch(url, { method: 'POST', headers: headers, body: JSON.stringify(credentials)})//{'username':'Asker','password':'Password'})})
      .then((res) => res.json())
      .then((res) => {
        console.log(JSON.stringify(res)+"=========="+res.name);
        localStorage.setItem('loginToken',JSON.stringify(res));
        if(res.status == 'Success')
          dispatch(loginSuccess(res));
        else
          dispatch(loginError(res));
        //Loading Online Users
        
      }).catch(error => {
        console.log('Error in Request ');
        dispatch(loginError(error));
      });
 };
};
export const executeLoginSuccess = (loginToken) => {
  return dispatch => {
    dispatch(loginSuccess(JSON.parse(loginToken)));
 };
};
export const loginSuccess = obj => {
  return {
    type: LOGIN_SUCCESS,
    payload: obj
  };
};
export const loginError = error => {
  return {
    type: LOGIN_FAIL,
    payload: error
  };
};

export const searchChatRoom = search => {
  return {
    type: SEARCH_CHATROOM,
    payload: search
  };
};

export const selectChatRoom = chatRoom => {
  return {
    type: CHAT_ROOM_SELECTED,
    payload: chatRoom
  };
};

export const selectChatRoomDispatcher = chatRoom => {
  return dispatch => {
    dispatch(selectChatRoom(chatRoom));
  };
};

export const sendChatMessage = message => {
  return {
    type: SEND_MESSAGE,
    payload: message
  };
};
//chatType USER/CHAT_ROOM
export const sendChatMessageAction = (
  token, message,
  fromId,
  toId,
  recepientType,
  fromName,
  toName
) => {
  const messageObj ={
    'message':message,
    'fromId':fromId,
    'toId':toId,
    'recepientType':recepientType,
    'fromName':fromName,
    'toName':toName,
    'token':token
  }
  //console.log('Sending Message : ', message);
  return dispatch => {
    console.log('Posting Login to Server');
    const url = window.location.origin+'/message';
    var headers = {
        "Content-Type": "application/json"
      }
      console.log(JSON.stringify(messageObj));
    fetch(url, { method: 'POST', headers: headers, body: JSON.stringify(messageObj)})//{'username':'Asker','password':'Password'})})
      .then((res) => res.json())
      .then((res) => {
        console.log("Message Sent =========="+res.status);
      }).catch(error => {
        console.log("Message Sent Failed:=========="+error);
        //dispatch(loginError(error));
      });
  };
};

export const myChatRoomList = res => ({
  type : MY_CHAT_ROOM_LIST,
  payload : res
});
/* Used only by actions for sockets */
export const usersListAction = res => ({
  type: USER_LIST,
  payload: res
});

export const loginUserAction = res => ({
  type: LOGGED_IN_USER,
  payload: res
});
export const newUserAction = res => ({
  type: NEW_USER,
  payload: res
});

export const newChatRoomAction = res => ({
  type: NEW_CHAT_ROOM,
  payload: res
});

export const newMessageAction = res => {
  return (dispatch, getState) => {
    const { loginToken, activeChatRoom } = getState();getState().loginToken.get('id')
    const msg = JSON.parse(res.body);
    if (
      CHAT_PERSON_TYPE_CHAT_ROOM == msg.recepientType ||
      (CHAT_PERSON_TYPE_USER == msg.recepientType &&
        (loginToken.get("id") == msg.toId ||
        loginToken.get("id") == msg.fromId))
    )
      dispatch({
        type: NEW_MESSAGE,
        payload: msg,
        activeChatRoom
      });
  };
};
export const createChatRoomAction = (token, chatRoomName) => {
  return dispatch => {
    const url = window.location.origin+'/chatroom/create';
    var headers = {
        "Content-Type": "application/json"
      }
    fetch(url, { method: 'POST', headers: headers, body: JSON.stringify({'token':token,'nickName':chatRoomName})})//{'username':'Asker','password':'Password'})})
      .then(res => {
        if(res.status!==200){
           throw new Error(res.status)
        }
        return res.json();
      })
      .then((res) => {
        if(res.status != 'Success')
          dispatch(loginError(res));        
        //   dispatch(newChatRoomAction(JSON.parse(res.group)));
        // else
        //   dispatch(loginError(res));        
      }).catch(error => {
        console.log('Error in Request ');
        dispatch(loginError(error));
      });
 };
};
export const joinChatRoom = (token, chatRoomId) =>{
  return dispatch => {
    const url = window.location.origin+'/chatroom/join';
    var headers = {
        "Content-Type": "application/json"
      }
    fetch(url, { method: 'POST', headers: headers, body: JSON.stringify({'token':token,'groupId':chatRoomId})})//{'username':'Asker','password':'Password'})})
      .then((res) => {
        if(res.status!==200){
           throw new Error(res.status)
        }
        return res.json();
      })
      .then((res) => {
        console.log("JOINED the  GROUP Response:"+JSON.stringify(res));
        dispatch(joinedAChatRoom(JSON.parse(res).groupId));
      }).catch(error => {
        console.log('Error in Request ');
        dispatch(loginError(error));
      });
 };
};
export const joinedAChatRoom = res => ({
  type: JOINED_A_CHATROOM,
  payload: res
});
export const loginToChatServerAction = res => ({
  type: CHAT_SERVER_LOGIN,
  payload: res
});

export const loadCurrentChatRoomList = socket => {
  return dispatch => {
    // socket.on("chatRoomList", res => {
    //   let { currentChatRoomListAction } = this.props;
    //   dispatch(currentChatRoomListAction(res));
    // });
  };
};

export const loginToChatServer = (socket, name) => {
  return dispatch => {
   // socket.emit("newUser", { name });
  };
};

export const logOffFromChatServer = socket => {
  return dispatch => {
  //  socket.emit("disconnect");
  };
};

import {SEARCH_CHATROOM, CURRENT_CHAT_ROOM_LIST, USER_LIST, NEW_USER, NEW_CHAT_ROOM, NEW_MESSAGE, CHAT_ROOM_SELECTED, CHAT_PERSON_TYPE_USER, JOINED_A_CHATROOM, MY_CHAT_ROOM_LIST} from '../actions/index.js'
import {List, fromJS} from 'immutable';

export default function(chatRoomList = List(), action){
  switch(action.type){
    case SEARCH_CHATROOM:
        return chatRoomList.map(room => room.get('nickName').toLowerCase().indexOf(action.payload.toLowerCase()) != -1 ? room.set('show', true):  room.set('show', false));
    case CURRENT_CHAT_ROOM_LIST:
        //console.log('CHAT_ROOM_REDUCER :Loading the Initial Chat Rooms from Socket', action.payload);
        return chatRoomList.push(...fromJS(action.payload));
    case USER_LIST:
        //console.log('CHAT_ROOM_REDUCER : USER_LIST :: ChatRoom List ', chatRoomList.push(fromJS([action.payload])))
        return chatRoomList.push(...fromJS(action.payload.userList
                    .map(
                        s=>{
                            s.show=true;
                            s.newMessage=false;
                            s.joined=false;
                            return s;})
                    .filter(s=>s.id != action.payload.me)));
    case NEW_USER:
        //console.log('CHAT_ROOM_REDUCER : NEW_USER :: ChatRoom List ', chatRoomList.push(fromJS([action.payload])))
        return chatRoomList.push(...fromJS([action.payload]));//[action.payload, ...chatRoomList];
    case NEW_CHAT_ROOM:
        //console.log('CHAT_ROOM_REDUCER : NEW_CHAT_ROOM :: ChatRoom List ', chatRoomList.push(fromJS([action.payload])))
        return chatRoomList.push(...fromJS([action.payload].map(s=>{s.show=true;s.newMessage=false;return s;})));//[action.payload, ...chatRoomList];
    case NEW_MESSAGE:
        // Updating both reciving end and sender end of Last Chat Time and new Message Indicator
        //PayLoad is the Message Object
        if(action.payload.recepientType == CHAT_PERSON_TYPE_USER){
          chatRoomList = chatRoomList.map(room => room.get('id') == action.payload.fromId && action.activeChatRoom.get('id') != room.get('id') ? room.set('newMessage', true): room)
          chatRoomList.map(room => room.get('id') == action.payload.fromId ? room.set('time', action.payload.time): room);
        }
        chatRoomList = chatRoomList.map(room => room.get('id') == action.payload.toId && action.activeChatRoom.get('id') != room.get('id') ? room.set('newMessage', true): room)
        return  chatRoomList = chatRoomList.map(room => room.get('id') == action.payload.toId ? room.set('time', action.payload.time): room)
    case CHAT_ROOM_SELECTED:
        //On Chat Room selected make the Chat Not BOLD
        //PayLoad is the ChatRoom Itself
        //console.log('Chat Room Selected ', action.payload);
        chatRoomList = chatRoomList.map(room => room.get('id') == action.payload.get('id') ? room.set('newMessage', false): room)
        //console.log('CHAT ROOM SELECTED :',chatRoomList)
        return chatRoomList
    case JOINED_A_CHATROOM:
        return chatRoomList.map(room => room.get('id') == action.payload ? room.set('joined', true): room)
    case MY_CHAT_ROOM_LIST:
            action.payload.forEach(g=>{
                chatRoomList = chatRoomList.map(room => room.get('id') == g.id.groupId ? room.set('joined', true): room);
            });
        return chatRoomList;
  }
  return chatRoomList;
}

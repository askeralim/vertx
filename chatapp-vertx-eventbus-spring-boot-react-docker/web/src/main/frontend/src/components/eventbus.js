import EventBus from "vertx3-eventbus-client";
import { newChatRoomAction, newMessageAction} from '../actions/index';
// const eventBus = new EventBus(window.location.origin+"/eventbus");
// eventBus.onopen = function() {
//     console.log("Home.js EventBus State:"+eventBus.state)
//     eventBus.registerHandler("chat.room", (err, message) => {
//       if (!err) {
//         dispatch(newChatRoomAction(JSON.parse(message.body)));
//       } else {
//         console.log("Error");
//       }
//     });
//     eventBus.registerHandler("chat.message", (err, message) => {//"chat."+self.props.loginToken.get('id')
//       if (!err) {
//         dispatch(newMessageAction(message));
//         console.log("Asker Message :"+message);
//       } else {
//         console.log("Asker :Error");
//       }
//     });
//     //loadUserDetails(dispatch, eb);
//   };
// console.log('NEW SOCKET CREATED ::::::::::::::::::::::::::::::::');

export default eventBus

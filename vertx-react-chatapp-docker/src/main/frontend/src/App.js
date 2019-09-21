import React, { Component } from "react";
import logo from "./logo.svg";
import "./App.css";
import EventBus from "vertx3-eventbus-client";
import axios from 'axios';

class App extends Component {
  constructor() {
    super();
    const msg = {
      'author':'Asker',
      'message':'Asker Message'
    };
    this.state = {
      data: {
        message:'',
        messages: [msg]
      }
    };
    this.eventBus = null;
    this.sendMessage = this.sendMessage.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  sendMessage() {
      const message = {
	      message: this.state.message,
	      author: "Asker"
	    };
    /** Commented the sending through EventBus
    console.log("Send Message :"+this.state.data.message);
    const eb = this.eventBus;

    console.log('Sending :'+JSON.stringify(message));
    this.setState({
      message: '',
      messages:this.state.messages
    });
    // Send message out
    eb.publish("chat.message", JSON.stringify(message));
    **/
    axios.post('/api/message', {
	      message: this.state.message,
	      author: "Asker"
	    });
    console.log("Message send");
  }
  handleChange(event) {
    const msg = {
      'author':'Asker',
      'message':event.target.value
    };
    //console.log(this.state);
    this.setState({
      message: event.target.value,
      messages:this.state.messages
    });
  }
  componentDidMount() {
    let data = this.state.data;
    this.eventBus = new EventBus("http://localhost:8080/eventbus");
    const eb = this.eventBus;
    const self = this;
    // Listen for messages coming in
    eb.onopen = function() {
      eb.registerHandler("chat.message", (err, message) => {
        if (!err) {
        	let msg = JSON.stringify(message);
			console.log("Received Message :" + msg);
			msg = msg.replace('"{','{').replace('}"','}');
			console.log("Parsed - Message :" + msg);
			msg = msg.replace(/'/g,'"');
			console.log("Parsed - Message :" + msg);
			msg = JSON.parse(msg);
			data = self.state.data;
			const messages = data.messages;
            messages.push(msg.body);
            self.setState({
              data: {
                message:self.state.message,
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
    //console.log("Rendering Messages List :"+this.state.data.messages);
    var listItems = '';
    if(this.state.data.messages){
      listItems = this.state.data.messages.map((msg,index) => <li key={index}>{msg.message}</li>);
     // console.log("Rendering :"+listItems);
    }
    return (
      <div className="App">
        <label>
          Message:
          <input type="text" name="message" value={this.state.message} onChange={this.handleChange}/>
        </label>
        <button onClick={this.sendMessage}>Send </button>
        {listItems }
      </div>
    );
  }
}
export default App;

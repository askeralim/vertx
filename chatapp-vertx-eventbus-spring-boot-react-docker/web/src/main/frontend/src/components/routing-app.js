import React, { Component } from 'react';
import { connect } from 'react-redux';

class RoutingApp extends Component {
  constructor(props){
    super(props);
    console.log("RoutingApp Props:"+JSON.stringify(props));
  }
  render(){
    return <div/>;
  }
  componentDidMount() {
    console.log('Component is Mounted');
    // if(!this.state.isLoggedIn)
    //   this.props.history.push("/login");
    // else
      this.props.history.push("/home");
  }
}
function mapStateToProps(state){
  return {
      isloggedIn : state.isLoggedIn
  }
}
export default connect(mapStateToProps)(RoutingApp);
//export default App;




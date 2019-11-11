import {LOGIN_SUCCESS,LOGIN_FAIL} from '../actions/index.js'
import {Map, fromJS} from 'immutable';
export default function(loginToken = Map(
    localStorage.getItem('loginToken')?JSON.parse(localStorage.getItem('loginToken')):null
  ), action){
  const res = fromJS(action.payload);
  switch(action.type){
    case LOGIN_SUCCESS:
        console.log("LOGIN SUCCESS :::::::::::::::::::: Logged In User ::::")
        return fromJS(action.payload)
    case LOGIN_FAIL:
        console.log("LOGIN FAILED :::::::::::::::::::: Logged In User ::::")
        return fromJS(action.payload)
  }
  return loginToken;
}

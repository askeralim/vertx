
var fetch = require('node-fetch');

const url = 'http://localhost/user/login';
//application/x-www-form-urlencoded"
var headers = {
    "Content-Type": "application/json"
  }
fetch(url, { method: 'POST', headers: headers, body: JSON.stringify({'username':'Asker Ali M','password':'Password'})})
  .then((res) => res.json())
    .then((res) => {
  console.log(JSON.stringify(res)+"=========="+res.name);
  // Do something with the returned data.
});
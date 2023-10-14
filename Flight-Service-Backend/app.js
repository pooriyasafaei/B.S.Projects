var express = require('express');
var path = require('path');
var bodyParser = require('body-parser');
var fs = require("fs");
var app = express();

// var {Client} = require("pg");

// //connect to Postgres
// const db = new Client({
//   user: 'postgres', //your pg user
//   host: 'localhost', 
//   database: 'HW2',  //your db
//   password: '1596324780', //your pg user password
//   port: 5432, //default port
// });

// db.connect(function(err) {
//   if (err) throw err;
//   console.log("Connected to DB!");
// });


//mongoose.connect("mongodb://localhost/fligth_reservation313");

// //register models
// fs.readdirSync(path.join(__dirname,"models")).forEach(function (filename) {
//     require("./models/"+filename);
// });
// app.use((req,resp,next)=>{
//     resp.header("Access-Control-Allow-Origin","*");
//     resp.header("Access-Control-Allow-Headers","Content-Type,x-access-token");
//     resp.header("Access-Control-Allow-Methods","GET,POST");
//     next();
//   });


// app.use((req,res,next)=>{
//   res.header("Access-Control-Allow-Origin","*");
//   res.header("Access-Control-Allow-Headers","Content-Type,x-access-token");
//   res.header("Access-Control-Allow-Methods","GET,POST,PUT,DELETE")
//   next();
// });

// //add body-parser middleware
// app.use(bodyParser.json());

var reservation = require('./controllers/reservation');
app.use('/reservation',reservation);

var transaction = require('./controllers/transaction');
app.use('/transaction',transaction);

var transaction = require('./controllers/detail');
app.use('/detail',transaction);
// var seat = require('./controllers/seat');
// app.use('/seat',seat);

//start listenning
app.listen(9090,function(){
    console.log("Starting....")
});

// module.exports = db;

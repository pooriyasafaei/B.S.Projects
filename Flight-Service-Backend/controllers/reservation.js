var express = require('express');
var router = express.Router();
var fs = require("fs");
var {Client} = require("pg");
var crypto = require('crypto');
var request = require('request');

//connect to Postgres
const database = new Client({
  user: 'postgres', //your pg user
  host: 'localhost', 
  database: 'HW2',  //your db
  password: '1596324780', //your pg user password
  port: 5432, //default port
});

database.connect(function(err) {
  if (err) throw err;
});

router.get("/transaction", async (req, res) => {

});

router.post("/", async (req, res) => {
    //required validations
     try{   
        let reservation = req.query;
        let reservation_list = JSON.parse(reservation.list);
        let flight;
        let user;

        try{
            user = await (get_user(reservation.user_id));
            flight = await (get_flight(reservation.flight_serial, reservation.class, reservation_list.length));

        } catch(err){
            res.status(400).send({message:"invalid input for reservation" ,code :400});
        }
        
        if(!user || user.length < 1){
            res.status(400).send({message:"invalid user error" ,code :400});
            console.log(user);
        }

        else if(!reservation_list || !reservation_list.length ||reservation_list.length < 1 || !validate_list(reservation_list)){
            res.status(400).send({message:"invalid input for passengers data" ,code :400});
            console.log(reservation_list);
        }

        
        else if(!flight || flight.length < 1){
            res.status(400).send({message:"flight is not available for reservation" ,code :400});
            console.log(flight);
        }

        else{
            console.log(flight[0]);
            console.log(reservation.class.toUpperCase());
            let flight_detail = get_amount(flight[0], reservation.class.toUpperCase(), reservation_list.length);
            res.status(200);
            transaction_req(user[0], flight[0], reservation_list, flight_detail.price, flight_detail.o_price, flight_detail.o_class, res);
        }
    }catch(err){
        console.log(err.stack);
        res.end();
    }
    
});


var get_user = async function(user_id){
    let user;
    let query = `SELECT * FROM user_account WHERE user_id = ${user_id};`;
    try{
        user = await database.query(query);

    }catch(err){
        console.log(err.stack);
    } 

    return user.rows;
}

var get_flight = async function(flight_serial, clas, number){
    let flight;
    let query = `SELECT * from available_offers WHERE flight_id = '${flight_serial}' AND ${clas.toLowerCase()}_class_free_capacity >= ${number};`;
    try{
        flight = await database.query(query);

    }catch(err){
        console.log(err.stack);
    }

    query = `SELECT * FROM flight WHERE flight_id = '${flight_serial}';`;
    try{
        let temp  = await database.query(query);
        flight.rows[0].flight_serial = temp.rows[0].flight_serial;
    }catch(err){
        console.log(err.stack);
    }

    return flight.rows;
}

var validate_list = function(useres_list){
    for(let i = 0; i < useres_list.length; i++)
        if(!useres_list[i].first_name || !useres_list[i].last_name) return false;
    return true;
}

var get_amount = function(flight, clas, number){

    let offer_class;
    let offer_price;
    let amount;
    if(clas == "Y"){
        offer_class = "Y";
        offer_price = flight.y_price;
        amount = flight.y_price * number;

    } else  if(clas == "J"){
        offer_class = "J";
        offer_price = flight.j_price;
        amount = flight.j_price * number;

    } else  if(clas == "F"){
        offer_class = "F";
        offer_price = flight.f_price;
        amount = flight.f_price * number;
    }

    return {price: amount, o_price: offer_price, o_class: offer_class};
}

var transaction_req = function(user, flight, reservation_list, price, offer_price, offer_class, res){
    let rec_id = Date.now() - Math.floor(Math.random() * 100000000);

    let postData = {
        'amount' : price,
        'receipt_id' : rec_id,
        'callback' : `http://127.0.0.1:9090/transaction/${rec_id}`
    };
    
    var clientServerOptions = {
        url: 'http://127.0.0.1:8000/transaction/',
        body: JSON.stringify(postData),
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }
    request(clientServerOptions, function (error, response) {
      let transaction_details =  JSON.parse(response.body);
        
      make_purchase(transaction_details, rec_id, user, flight, reservation_list, offer_price, offer_class, res);
    })
}

var make_purchase  = function(transaction_details, receipt_id, user, flight, reservation_list, offer_price, offer_class, res){
    let transaction_id = transaction_details.id;

    reservation_list.forEach(element => {
        add_purchase(transaction_id, receipt_id, user, flight, offer_price, offer_class, element.first_name, element.last_name);
    });

    res.redirect(`http://127.0.0.1:8000/payment/${transaction_id}`);
}

var add_purchase = function(transaction_id, receipt_id, user, flight, offer_price, offer_class, first_name, last_name){
    let query = `INSERT INTO purchase(corresponding_user_id , title, first_name, last_name, flight_serial, offer_price, offer_class, transaction_id, transaction_result)
    VALUES (${user.user_id}, ${receipt_id}, '${first_name}', '${last_name}', ${flight.flight_serial}, ${offer_price}, '${offer_class}',  ${transaction_id}, 0 );`
    try{
        database.query(query);
        }catch(err){
            console.log(err.stack);
        }
}


module.exports = router;
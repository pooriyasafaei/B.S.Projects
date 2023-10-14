var express = require('express');
var router = express.Router();
var fs = require("fs");
var {Client} = require("pg");

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

router.get('/:receipt/:status', async (req, res) => {
  let purchase = await set_purchase_status(req.params.receipt, req.params.status);
  res.status(200).send({message:`Transaction status for receipt ${req.params.receipt} updated with status: ${req.params.status}`, purchases: JSON.stringify(purchase)});
});

var set_purchase_status = async function(purchase_id, status){
  let query = `Update purchase SET transaction_result = ${status} WHERE title = '${purchase_id}' RETURNING *;`;
  console.log(query);
  let purchase;
  try{
      purchase = await database.query(query);
      return purchase.rows;

  }catch(err){
      console.log(err.stack);
  } 

}

module.exports = router;
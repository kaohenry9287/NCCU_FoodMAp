const express = require('express');
const app = express();
const axios = require('axios');
const cors = require('cors');
const mapRouter = require('./routers/map');
const restaurantsRouter = require('./routers/restaurants');
const Response = require('./models/Response');

const config = require('./config');
// require("./db/mysql"); //trigger connect SQL

var port = process.env.PORT | 3000;

app.use(express.json());
app.use(cors());

app.use('/map', mapRouter);
app.use('/restaurants', restaurantsRouter);

//register view engine
app.set('view engine', 'ejs');

app.get('/test', (req, res)=>{
    res.json(new Response(true, 200, "test route"));
});

app.post('/type', async (req, res)=>{
    const typeDatabase = require('./db/type');
    let datas = await typeDatabase.searchRestaurant();
    Object.keys(datas).forEach(type=>{
        datas[type].forEach(data=>{
            typeDatabase.writeInDatabase(type, data.id, data.name);
        });
    });
    res.json(new Response(true, "insert type data successfully", datas));
});

app.listen(port, ()=>{
    console.log("server listen on "+port);
});
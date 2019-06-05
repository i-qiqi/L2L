const request       = require('request');

let synchronous_post = function (url , params ){

    let options = {
        url     : url ,
        form    : params
    };

    request.get(options , function(error,response,body){
        console.log(body);
    });
}

let demo = function(){
    let url = "http://l2l.soaringlab.top:8084/hello";
    synchronous_post(url);
}

demo();


// // Promise、await、async异步变同步

// const request       = require('request');

// let synchronous_post = function (url , params ){

//     let options = {
//         url     : url ,
//         form    : params
//     };

//     return new Promise(function(resolve, reject){
//         request.get(options , function(error,response,body){
//             if(error){
//                 reject(error);
//             }else{
//                 resolve(body);
//             }
//         });
//     });
// }

// let demo = async function(){
//     let url = "http://www.baidu.com/";
//     let body = await synchronous_post(url);
//     console.log(body);
// }

// demo();

require.config({
    baseUrl: 'static',
    // alias libraries paths
    paths: {
        'jquery': 'plugins/jquery-3.1.1.min',
        jqueryui: 'plugins/jquery-ui.min',
        'underscore': 'plugins/underscore-min',
        'backbone': 'plugins/backbone-min',
        'bootstrap': 'plugins/bootstrap/js/bootstrap.min',
        text: 'plugins/text/text',
        approuter: 'envite/js/router'
    },

    // following plugins are not support AMD out of the box, put it in a shim
    shim: {
        underscore: {
          exports: "_"
        },
        'jquery': {
            exports: "jQuery"
        },
        backbone: {
          deps: ['underscore', 'jquery'],
          exports: 'Backbone'
        },
        'bootstrap': {
            deps: ['jquery','jqueryui']
        },
        'jqueryui': {
            deps: ['jquery']
        }
    },
});
require([
    'jquery',
    'backbone',
    'bootstrap',
    'approuter'
],function($,Backbone,bootstrap,Router){
    window.router = new Router;
    Backbone.history.start();
});
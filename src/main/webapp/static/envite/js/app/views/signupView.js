define([
    'jquery',
    'underscore',
    'backbone',
    'text!envite/js/app/templates/signupTemplate.html',
], function ($, _, Backbone, signupTemplate) {
    'use strict';
    return Backbone.View.extend({
        el: '.signup-view',
        initialize: function (opts) {
            this.model = opts.model;
        },
        events:{
            'click .login-button': 'renderLoginView',
            'click .signup-button': 'doSignup'
        },
        render: function () {
            var _signupTemplate= _.template(signupTemplate, {});
            this.$el.append(_signupTemplate);
        },
        renderLoginView:function(ev){
            ev.preventDefault();
            window.router.navigate('#login', {
                trigger: true
            });
            return false;
        },
        doSignup: function(ev){
            ev.preventDefault();
            var data = this.$el.find('.signup-form').serialize();
            $.ajax({
                url: "/ev/registerUser",
                type: "POST",
                context: this,
                data: data,
                success: function (data, textStatus, request) {
                    var jsonData = JSON.parse(data);
                    if (!!jsonData.error) {
                        this.$el.find('.error-message').text(jsonData.error);
                    } else {
                        // set the userJSON
                        var userJSON = window.userJSON = jsonData.userJSON;
                        // Set the route to Dashboard and no navigation
                        window.router.navigate(''); //Below redirect will not work, if it was original URL, hence, navigating blank & not triggering
                        window.router.navigate('#dashboard/close', {trigger: true});
                    }
                },
                error: function () {
                },
                complete: function () {
                }
            });
        }
    });
});
define([
    'jquery',
    'underscore',
    'backbone',
    'text!envite/js/app/templates/loginTemplate.html',
], function ($, _, Backbone, loginTemplate) {
    'use strict';
    return Backbone.View.extend({
        el: '.login-view',
        initialize: function (opts) {
            this.model = opts.model;
        },
        events:{
            'click .signup-button': 'renderSignUpView',
            'click .login-submit-button': 'submitLoginHandler'
        },
        render: function () {
            var _loginFormTemplate = _.template(loginTemplate, {});
            this.$el.append(_loginFormTemplate);
        },
        renderSignUpView:function(ev){
            ev.preventDefault();
            window.router.navigate('#signup', {
                trigger: true
            });
            return false;
        },
        submitLoginHandler: function(ev){
            ev.preventDefault();
            var data = this.$el.find('.login-form').serialize();
            $.ajax({
                url: "/ev/login",
                type: "POST",
                context: this,
                data: data,
                success: function (data, textStatus, request) {
                    this.renderAuthenticatedUserNavigation(data);
                },
                error: function () {
                },
                complete: function () {
                }
            });
        },
        renderAuthenticatedUserNavigation: function (data) {
            var jsonData = JSON.parse(data);
            if (!!jsonData.error) {
                this.$el.find('.error-message').text(jsonData.error);
            } else {
                // set the userJSON
                var userJSON = window.userJSON = JSON.parse(data).userJSON;
                // Set the route to Dashboard and no navigation
                window.router.navigate(''); //Below redirect will not work, if it was original URL, hence, navigating blank & not triggering
                window.router.navigate('#dashboard/close', {trigger: true});
            }
        }
    });
});
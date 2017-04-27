define([
    'jquery',
    'underscore',
    'backbone',
    'envite/js/app/views/MainView'
], function ($, _, Backbone, MainView) {
    'use strict';
    var Router = Backbone.Router.extend({
        routes: {
          "": "home",
          ":selected":"home",
          ":selected/:close":"home",
          ":selected(/:close)(/:p1)(/*params)":"home",
        },

        home: function(selected, close, p1, params){
            if (typeof(this.appStart) === "undefined" || ((selected === "login" || selected === "signup") && !!window.userJSON)) {
                $.ajax({
                    async: false,
                    cache: false,
                    dataType: 'json',
                    context: this,
                    url: "ev/isAuthenticated",
                    success: function (response) {
                        if ($.isEmptyObject(response)){
                            if (selected != 'signup') {
                                selected = "login";
                                Backbone.history.navigate("#login");
                            }
                        } else {
                            this.appStart = true;
                            var userJSON = window.userJSON = response.userJSON;
                            if (selected === "login" || selected === "signup") {
                                selected = "dashboard";
                                window.router.navigate('#dashboard');
                            }
                        }
                        this.initView(selected, close, p1, params);
                    }
                });
            } else {
                this.initView(selected, close, p1, params);
            }
        },
        initView: function(selected, close, p1, params){
            this.appStart = true;
            var $body = $('body');
            if(selected !== "login" && selected !== "signup"){
                var $content = $("#container");
                $content.empty();
                this.mainView && this.mainView.close();
                var mainView = new MainView({
                    el: $("<div/>").appendTo($content),
                    shellView: this.shellView
                });
                this.mainView = mainView;
                mainView.selected = selected;
                mainView.render();
            } else {
                switch (selected) {
                    case 'login':
                        $body.find('#container').html('<div class="login-view"></div>');
                        require(['envite/js/app/views/loginView'], $.proxy(function(LoginView){
                            // Set the layout
                            var loginView = new LoginView({
                                model: new Backbone.Model({'mode': '', 'params': p1}),
                            });
                            //$content = $("#content", asLoginView.el);
                            loginView.render();
                        }, this));
                        break;
                    case 'signup':
                        $body.find('#container').html('<div class="signup-view"></div>');
                        require(['envite/js/app/views/signupView'], $.proxy(function (SignupView) {
                            var signupModel = new Backbone.Model({
                                'mode': '',
                                'selected': selected,
                                'params': p1
                            });
                            var signupView = new SignupView({
                                model: signupModel
                            });
                            signupView.render();
                        }, this));
                        break;
                    default :
                        break;
                }
            }
        }
    });
    return Router;
});
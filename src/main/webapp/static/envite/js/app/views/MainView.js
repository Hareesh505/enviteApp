define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!envite/js/app/templates/mainView.html'
], function ($, _, Backbone, bootstrap, mainViewTemplate) {
    'use strict';
    return Backbone.View.extend({
        initialize: function (opts) {
            this.selected = opts.selected;
        },
        render: function () {
            var _mainViewTemplate = _.template(mainViewTemplate);
            this.$el.html(_mainViewTemplate({"userData": window.userJSON}));
            switch (this.selected) {
                case 'dashboard':
                    require(['text!envite/js/app/templates/dashBoardTemplate.html'], $.proxy(function(dashBoardTemplate){
                        var _dashBoardTemplate = _.template(dashBoardTemplate);
                        this.$el.find('#main-section').html(_dashBoardTemplate( {"userData": window.userJSON}));
                    }, this));
                    break;
                case 'profile':
                    require(['envite/js/app/views/profileView'], $.proxy(function(ProfileView){
                        var profileView = new ProfileView({
                            model: new Backbone.Model({
                               'userData': window.userJSON
                            })
                        });
                        profileView.render();
                        this.$el.find('#main-section').html(profileView.$el);
                    }, this));
                    break;
                default :
                    break;
            }
        }
    });
});
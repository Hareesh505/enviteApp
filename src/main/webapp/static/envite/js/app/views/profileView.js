define([
    'jquery',
    'underscore',
    'backbone',
    'text!envite/js/app/templates/profileTemplate.html',
], function ($, _, Backbone, profileTemplate) {
    'use strict';
    return Backbone.View.extend({
        //el: '.signup-view',
        initialize: function (opts) {
            this.model = opts.model;
        },
        events:{
            'click .update-button': 'updateProfile',
            'click .edit-profile': 'enableEditView',
            'click .cancel-button': 'disableEditView'
        },
        render: function () {
            var _profileTemplate= _.template(profileTemplate, {});
            this.$el.html(_profileTemplate({'userData':this.model.get('userData')}));
            this.$el.find('.edit-view').hide();
            this.$el.find('.read-view').show();
        },
        enableEditView: function(ev){
            ev.preventDefault();
            this.$el.find('.edit-view').show();
            this.$el.find('.read-view').hide();
        },
        disableEditView: function(ev){
            ev.preventDefault();
            this.$el.find('.edit-view').hide();
            this.$el.find('.read-view').show();
        },
        updateProfile: function(ev){
            ev.preventDefault();
            var data = this.$el.find('.profile-update-form').serialize();
            $.ajax({
                url: "/ev/updateUser",
                context: this,
                data: data,
                success: function (data, textStatus, request) {
                    var jsonData = JSON.parse(data);
                    if (!!jsonData.error) {
                        this.$el.find('.error-message').text(jsonData.error);
                    } else {
                        // set the userJSON
                        var userJSON = window.userJSON = jsonData.userJSON;
                        this.model.set("userData",jsonData.userJSON);
                        this.render();
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
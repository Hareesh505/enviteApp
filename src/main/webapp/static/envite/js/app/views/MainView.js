define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!envite/js/app/templates/dashBoardTemplate.html'
], function ($, _, Backbone, bootstrap, dashBoardTemplate) {
    'use strict';
    return Backbone.View.extend({
        initialize: function (opts) {
            this.selected = opts.selected;
        },
        render: function () {
            switch (this.selected) {
                case 'dashboard':
                    var _dashBoardTemplate = _.template(dashBoardTemplate);
                    this.$el.html(_dashBoardTemplate( {"userData": window.userJSON}));
                    break;
                default :
                    break;
            }
        }
    });
});
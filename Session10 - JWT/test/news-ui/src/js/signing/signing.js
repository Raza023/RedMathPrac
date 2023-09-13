(function() {
    'use strict';

    function SigningController($http, $location) {
        var self = this;
        self.user = {};

        self.login = function() {
            $http.post('news/login',
                'username=' + self.user.name + '&password=' + self.user.password, {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            }).then(self.loginSuccess, self.loginFailure);
        }

        self.loginSuccess = function(response) {
            $location.path('/news');
        }

        self.loginFailure = function(response) {
            self.user.error = response.data.message;
        }

        self.logout = function() {
            $http.post('news/logout').then(self.logoutSuccess, self.logoutFailure);
        }

        self.logoutSuccess = function(response) {
            $location.path('/search');
        }

        self.logoutFailure = function(response) {
            self.user.error = response.data.message;
        }
    }

    angular.module('news-fe').controller('SigningController', ['$http', '$location', SigningController]);

}());
(function() {
    'use strict';

    function NewsConfig($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider.when('/login', {
            templateUrl: 'signing/login.html',
            controller: 'SigningController as $ctrl'
        }).when('/logout', {
            templateUrl: 'signing/logout.html',
            controller: 'SigningController as $ctrl'
        }).when('/news', {
            templateUrl: 'news/news.html',
            controller: 'NewsController as $ctrl'
        }).when('/search', {
            templateUrl: 'news/search.html',
            controller: 'NewsController as $ctrl'
        }).otherwise('/search');
    }

    angular.module('news-fe', ['ngRoute', 'ngResource', 'ng'])
        .config(['$locationProvider', '$routeProvider', NewsConfig]);

}());
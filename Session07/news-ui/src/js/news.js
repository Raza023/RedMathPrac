(function () {
    'use strict';

    //Application register and define what are the dependencies you wanna use.
    angular.module("news-fe", ['ngResource', 'ng']);

    //$resource is dependency injection against server side resource.
    //:id is optional and will handle one record get request, update, delete
    //we always have to pass relative path.
    //check this out: https://docs.angularjs.org/api/ngResource/service/$resource  for the examples of get, save(post), put, delete requests
    //Service define
    function NewsService($resource) {
        return $resource('http://localhost:9080/api/v1/news/search:id');
    }

    //Service register
    angular.module('news-fe').factory('newsService', ['$resource', NewsService]);

    //News Controller (It will bind this view to backend resources.)
    //equivalent to class
    function NewsController(newsService) {
        var self = this;

        self.service = newsService;
        self.accounts = [];
        self.title = '';
        self.display = false;            //for performance improvement.

        self.init = function () {
            self.search();
        }

        self.search = function () {
            self.display = false;
            var parameters = {};
            if (self.title) {
                parameters.search = '%'+self.title+'%';
            }
            else
            {
                parameters.search = '%';
            }

            self.service.get(parameters).$promise.then(function (response) {
                self.display = true;
                self.news = response.content;
            });
        }

        self.init();
    }

    //Controller register  (pass dependency of service.)
    angular.module("news-fe").controller('newsController', ['newsService', NewsController]);

}());
'use strict';

angular.module('compojurepoc', ['ngResource', 'ngRoute'])

  .config(function($routeProvider) {
    $routeProvider.when('/', {
      controller : 'compojurepocController',
      templateUrl : 'section1.html'
    })
  })

  .controller('compojurepocController', ['$scope', 'testDataResource', function($scope, testDataResource) {
    $scope.testData = testDataResource.fetchData();
  }])

  .factory('testDataResource', ['$resource', function($resource) {
    var resource = $resource('api/testdata', null, {
      fetchData : {
        method: 'GET'
      }
    });

    return {
      fetchData : resource.fetchData
    }
  }]);
'use strict';

angular.module('compojurepoc', ['ngResource', 'ngRoute'])

  .config(function($routeProvider) {
    $routeProvider.when('/', {
      controller : 'questionController',
      templateUrl : 'question.html'
    });
  })

  .controller('questionController', ['$scope', 'dataResource', function($scope, dataResource) {
    var answers = [];

    $scope.submitAnswer = function() {

      var currentQuestionData = $scope.questionData[$scope.currentQuestionIndex];
      answers.push({questionid: currentQuestionData.questionid,
                    answerindex: parseInt($scope.selectedAnswer)});

      if($scope.currentQuestionIndex === $scope.questionData.length - 1 ) {
        dataResource.checkData({'answers' : answers}, function(results) {
          $scope.done = true;
          $scope.correct = results.correct.length;
        });
      } else {
        $scope.currentQuestionIndex++;
      }
    }

    $scope.currentQuestionIndex = 0;
    $scope.questionData = dataResource.fetchData();

  }])

  .factory('dataResource', ['$resource', function($resource) {
    var resource = $resource(null, null, {
      fetchData : {
        method: 'GET',
        isArray : true,
        url : 'api/testdata'
      },
      checkData : {
        method : 'POST',
        url : 'api/checkdata'
      }
    });

    return {
      fetchData : resource.fetchData,
      checkData : resource.checkData
    }
  }]);
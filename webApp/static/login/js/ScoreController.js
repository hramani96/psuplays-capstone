'use strict';
var app = angular.module('signup')

app.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.xsrfCookieName = 'csrftoken';
	$httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('ScoreController', ['$http', '$window', '$scope', '$rootScope', '$interval', '$modal', 'Auth', 'AUTH_EVENTS', function($http, $window, $scope, $rootScope, $interval, $modal, Auth, AUTH_EVENTS) {

	var vm = this;


	vm.index = function() {
	}

	

	vm.reset = function() {
		vm.formInfo = {};
	}



	vm.getActiveGames = function() {
		$http.get('/score/getActiveGames')
		.then(function success(response) {
				vm.games = response.data.games;
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}
	vm.create = function() { 
		$http.post('/score/createGame/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success("Game has started successfully");
				vm.reset();
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});
	}
	
	vm.end = function(index) {
		vm.formInfo = vm.games[index];
		$http.post('/score/endGame/', vm.formInfo)
		.then(function success(response) {
				console.log(response);
				toastr.success("Game has ended successfully");
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});	
	}

	vm.update = function() {
	}
	
	vm.team1Decrease = function(index) {
		vm.formInfo = vm.games[index];
		vm.formInfo.score_1--;
		$http.post('/score/update/', vm.formInfo)
		.then(function success(response) {
				console.log(response);
				toastr.success("Score update has been approved");
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});	
	}
	vm.team2Decrease = function(index) {
		vm.formInfo = vm.games[index];
		vm.formInfo.score_2--;
		$http.post('/score/update/', vm.formInfo)
		.then(function success(response) {
				console.log(response);
				toastr.success("Score update has been approved");
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});	
	}
	vm.team1Increase = function(index) {
		vm.formInfo = vm.games[index];
		vm.formInfo.score_1++;
		$http.post('/score/update/', vm.formInfo)
		.then(function success(response) {
				console.log(response);
				toastr.success("Score update has been approved");
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});	
	}
	vm.team2Increase = function(index) {
		vm.formInfo = vm.games[index];
		vm.formInfo.score_2++;
		$http.post('/score/update/', vm.formInfo)
		.then(function success(response) {
				console.log(response);
				toastr.success("Score update has been approved");
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});	
	}


	
	if ($window.sessionStorage["userInfo"]) {
		var credentials = JSON.parse($window.sessionStorage["userInfo"]);
		console.log("Page refreshed, maintained info: "+$window.sessionStorage["userInfo"]);
		console.log(credentials);
		Auth.login(credentials);
	}
	
	
	$interval(vm.getActiveGames, 5000); //refreshes table scores every 5 seconds
}]);


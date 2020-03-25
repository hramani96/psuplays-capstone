var app = angular.module('signup', []);

app.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.xsrfCookieName = 'csrftoken';
	$httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('TeamController', function($http, $window, $scope) {

	var vm = this;


	vm.admins = []

	vm.admin = []

	vm.formInfo = {};

	vm.index = function() {
	}

	//vm.show = function(admin) {
	//    vm.admin = admin;
	//    console.log(vm.admin);
	//    //return vm.admin;
	//}

	vm.reset = function() {
		vm.formInfo = {};
	}

	vm.getAllTeams = function() {
		$http.get('/team/getAllTeams')
			.then(function success(response) {
				vm.teams = response.data.teams;
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}

	vm.create = function() {
		$http.post('/team/create/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success("Team has been submitted");
				$window.location.href='/student/team/createTeam/';
				//vm.reset();
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});
	}

	vm.update = function() {
	}

	vm.delete = function() {
	}

	vm.getNewTeams = function() {
		$http.get('/team/getNewTeams')
			.then(function success(response) {
				vm.teams = response.data.teams;
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}

	vm.getApprovedTeams = function() {
		$http.get('/team/getApprovedTeams')
			.then(function success(response) {
				vm.teams = response.data.teams;
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}

	vm.approve = function(index) {
		vm.formInfo = vm.teams[index];
		$http.post('/team/ApproveTeam/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success("Team has been approved");
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});	
	}

	vm.deny = function(index) {
		vm.formInfo = vm.teams[index];
		$http.post('/team/DenyTeam/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success("Team has been denied");
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});	
	}

});

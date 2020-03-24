var app = angular.module('signup', []);

app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = 'csrftoken';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('UserController', function($http, $window, $scope) {

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

    vm.getAllAdmins = function() {
        $http.get('/admin/getAllAdmins')
        .then(function success(response) {
            vm.admins = response.data.admins;
        }, function error(response) {
            console.log(response);
            toastr.error("Failure : " + response.data.reason);
        });
    }

    vm.create = function() {
        $http.post('/user/create/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" Account has been created");
            if (vm.formInfo.role = "Admin") {
                vm.getallAdmins();
                vm.reset();
            }
        },
        function error(response) {
            console.log(response);
            toastr.error("Failure : " + response.data.reason);
        });
    }
	
	vm.createTeam = function() {
		$http.post('/team/create/', vm.formInfo)
		.then(function success(response) {
			console.log(response);
			toastr.success("Team has been submitted");
			vm.reset();
		},
		function error(response) {
			console.log(response);
			toastr.error("Failure : " + response.data.reason);
		});
	}
	
    vm.update = function() {
    }

    vm.delete = function(admin) {
        $http.post('/user/remove/', admin)
        .then(function success(response) {
            console.log(response);
            toastr.success(" Admin has been removed");
        }, function error(response) {
            console.log(response);
            toastr.error("Failure : " + response.data.reason);
        });
    }

    vm.login_student = function() {
        $http.post('/loginStudent/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" login successful");
            $window.location.href='/dashboard/';
        },
        function error(response) {
            console.log(response);
            toastr.error("failure : " + response.data.reason);
        });
    }

    vm.login_admin = function() {
        $http.post('/loginAdmin/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" login successful");
            $window.location.href='/admin/dashboard/';
        },
        function error(response) {
            console.log(response);
            toastr.error("failure : " + response.data.reason);
        });
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

var app = angular.module('signup', []);

app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = 'csrftoken';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('SportController', function($http, $window) {

    var vm = this;

    vm.sports = []

    vm.sport = []

    vm.formInfo = {};

    vm.index = function() {
    }

    vm.show = function(admin) {
    }

    vm.reset = function() {
        vm.formInfo = {};
    }

    vm.getAllSports = function() {
        $http.get('/sport/getAllSports')
        .then(function success(response) {
            vm.sports = response.data.sports;
        }, function error(response) {
            console.log(response);
            toastr.error("Failure : " + response.data.reason);
        });
    }

    vm.create = function() {
        $http.post('/sport/create/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" Sport has been created");
		$window.location = "/admin/sports/" 
	
        },
        function error(response) {
            console.log(response);
            toastr.error("Failure : " + response.data.reason);
        });
    }

    vm.update = function() {
    }

    vm.delete = function(admin) {
        $http.post('/sport/remove/', admin)
        .then(function success(response) {
            console.log(response);
            toastr.success(" Sport has been removed");
		$window.location = "/admin/sports/" 
        }, function error(response) {
            console.log(response);
            toastr.error("Failure : " + response.data.reason);
        });
    }
});

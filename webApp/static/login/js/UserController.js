var app = angular.module('signup', []);

app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = 'csrftoken';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('UserController', function($http, $window) {

    var vm = this;


    vm.admins = []

    vm.admin = []

    vm.formInfo = {};

    vm.index = function() {
    }

    vm.show = function(admin) {
        vm.admin = admin;
        console.log(vm.admin);
        //return vm.admin;
    }

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

});

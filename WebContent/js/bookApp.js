var bookApp = angular.module('bookApp', []);
bookApp.controller('myController', ['$scope', function ($scope) {
    $scope.hey = "hi";
}]);

bookApp.value('userData', {
    isLogged: false,
    loggedUser: "",
    adminLogged: false,
    getPurchasedBooksFunc: null, //? // TODO: check
    adminMode: null,
    getAllBooksFunc: null,
    getNewReviewsFunc: null,
    newRevies: 0,
    purchasedBooks: [], // update at login
    booklistnames: []
});

bookApp.controller('bookController', ['$scope', '$http', '$filter', 'userData',
    function ($scope, $http, $filter, userData) {
        $scope.booklistView = {
            All: false,
            Purchased: false,
            singleBook: false
        };
        $scope.bookUserData = userData; //isLogged, loggedUser, adminLogged


        var getAllBooks = function getAllBooksInnerFunc() {
            $http.get("BooksServlet/getAllBooks").then(function (response) {
                var booklistAll = response.data;
                $scope.booklistnames = [];
                //$scope.booklistView.All = true;
                angular.forEach(booklistAll, function (item) {
                    this.push(item.Name);
                }, $scope.booklistnames);
                $scope.booklist = response.data;
                userData.booklistnames = $scope.booklistnames;
                //return response.data;
            },
            function (response) {});
        };

        $scope.booklistnames = [];
        getAllBooks();
        userData.getAllBooksFunc = getAllBooks;

        $scope.allbooks = function () {
            $scope.booklistView.All = true;
            $scope.booklistView.Purchased = false;
            $scope.booklistView.singleBook = false;
            //$scope.booklist = bookListInit;
            // $http.get("BooksServlet/getAllBooks").then(function (response) {
            //         $scope.booklist = response.data;
            //         $scope.booklistView.All = true;
            //     },
            //     function (response) {});
        };
        $scope.purchasedbooks = function () {
            //only logged
            if (!$scope.bookUserData.isLogged) {
                console.log("purchased books - only for logged user");
                return;
            }
            $http.get("BooksServlet/myBooks").then(function (response) {
                    //$scope.purchasedBooklist = response.data;
                    $scope.bookUserData.purchasedBooks = response.data;
                    $scope.userBoughtThisBook = function (bookname) {
                        if (($scope.bookUserData.isLogged === false) || angular.isUndefined(bookname))
                            return false;
                        var bookFound = $filter('filter')($scope.bookUserData.purchasedBooks, {
                            Name: bookname
                        }, true)[0];
                        if (bookFound) { // user bought it
                            return true;
                        } else
                            return false;
                        // $filter('filter')(myArray, {'id':73})
                        // if $scope.book.Name
                    };
                },
                function (response) {
                    // no books / not logged.
                    console.log("Not logged in");
                });
            $scope.booklistView.Purchased = true;
            $scope.booklistView.All = false;
            $scope.booklistView.singleBook = false;
        };

        userData.getPurchasedBooksFunc = $scope.purchasedbooks; // TODO: check

        $scope.userLikedThisBook = function (likesList) {
            if (($scope.bookUserData.isLogged === false) || angular.isUndefined(likesList))
                return false;
            var likeFound = $filter('filter')(likesList, {
                Username: $scope.bookUserData.loggedUser
            }, true)[0];
            if (likeFound) { // user bought it
                return true;
            } else
                return false;
            // $filter('filter')(myArray, {'id':73})
            // if $scope.book.Name
        };
        $scope.singlebook = function () {
            var chosenBook = $scope.chosenSingleBook;
            if (angular.isUndefined(chosenBook)) {
                $scope.singlebook = null;
                return;
            }
            //$scope.singleBook = $scope.booklist.get({Name: chosenBook});
            $scope.singleBook = $filter('filter')($scope.booklist, {
                Name: chosenBook
            }, true)[0];
            // change view
            $scope.booklistView.All = false;
            $scope.booklistView.singleBook = true;
            $scope.booklistView.Purchased = false;
        };

        $scope.readBook = function (bookHtml) {
            $scope.booklistView.All = false;
            $scope.booklistView.singleBook = false;
            $scope.booklistView.Purchased = false;

            //var testBook = "An Everyday Girl—A Project Gutenberg eBook.html";
            $scope.readMode = true;
            $scope.bookReadOn = "book list/" + bookHtml;

            //get location from db to:
            $scope.readModeFuncs.locationOnPage = 0;
        };

        $scope.readModeFuncs = {
            savePosition: function () {
                $scope.readModeFuncs.locationOnPage = window.scrollY;
                //servlet to db.
            },
            goBack: function () {
                window.scrollTo(0, $scope.readModeFuncs.locationOnPage);
            },
            stopReading: function () {
                $scope.readMode = false;
                $scope.bookReadOn = null;
                return;
            },
            saveAndStop: function () {
                $scope.readModeFuncs.savePosition();
                $scope.readModeFuncs.stopReading();
            }
        };

        $scope.bookToReview = "";
        $scope.bookToReviewId = 0;
        $scope.addReviewShow = false;
        $scope.addReviewFinish = false;

        // review
        $scope.addReviewOpen = function (bookName, id) {
            $scope.bookToReview = bookName;
            $scope.bookToReviewId = id;
            $scope.addReviewShow = !$scope.addReviewShow;
        };
        $scope.addReviewClose = function () {
            if ($scope.addReviewShow === true) {
                $scope.bookToReview = "";
                $scope.bookToReviewId = 0;
                $scope.newReview = "";
                $scope.addReviewShow = !$scope.addReviewShow;
                $scope.addReviewFinish = false;
            }
        };
        // $scope.addReview = function (bookName, reviewText) {
        $scope.addReview = function () {
            var reviewText = $scope.newReview;
            var bookid = $scope.bookToReviewId;
            //"Username": "user01","IdBook": "1", "dateWritten": "1280016860145","isApproved": "1","dateApproved": "1280296010145","reviewText":
            var obj = {
                Username: userData.loggedUser,
                IdBook: bookid,
                reviewText: reviewText,
            };
            $scope.addReviewFinish = true;

            $http.post("ReviewServlet/book/"+bookid, obj).then(function (response) {
                    // response.data
                    // should response updated reviews list for this book!
                    $scope.reviewFinishText = "Review sent, waiting for admin approval";
                }, function () {
                    $scope.reviewFinishText = "Review sending failed";
                }

            );
        };

        $scope.bookToBuy = "";
        $scope.bookToBuyId = null;
        $scope.buyBookFormShow = false;
        $scope.buyBookFinish = false;

        var initialBuyBook = {
            type: undefined,
            visa: undefined,
            csv: undefined,
            expiry: {
                month: undefined,
                year: undefined
            }
        };
        //$scope.buyBook = angular.copy(initialBuyBook);

        $scope.buyBookOpen = function (bookname, id) {
            $scope.bookToBuy = bookname;
            $scope.bookToBuyId = id;
            ////$scope.buyBook.bookname = bookname;
            $scope.buyBookFormShow = true;
        };
        $scope.buyBookClose = function () {
            if ($scope.buyBookFormShow === true) {
                $scope.bookToBuy = "";
                $scope.bookToBuyId = null;
                $scope.buyForm.$setPristine();
                $scope.buyForm.$setUntouched();
                $scope.buyBook = angular.copy(initialBuyBook);
                $scope.buyBookFormShow = !$scope.buyBookFormShow;
                $scope.buyBookFinish = false;
            }
        };

        $scope.buyBookFunc = function () {
            if ($scope.buyForm.$invalid) {
                return;
            }
            // var obj = {
            //     IdBook: bookToBuyId,
            //     Username: "",
            //     DateBought: null
            // };
            //$scope.buyBook.bookName = bookname;
            $scope.buyBookFinish = true;
            var bookId = $scope.bookToBuyId;

            //TOOD: support actual "$scop.buyBook" object with all details

            // $http.post("BuyingServlet/book/" + $scope.bookToBuy, $scope.buyBook).then(function (response) {
            $http.post("Purchase/book/" + bookId).then(function (response) {
                    $scope.purchasedbooks(); // update purchased books list
                    $scope.buyBookFinishText = "Book purchase completed";
                }, function () {
                    $scope.buyBookFinishText = "Book purchase failed";
                }

            );
        };

        function getLikesForBook(book) {
            $http.get("LikeServlet/book/" + book.IdBook).then(function (response) {
                book.likes = response.data;
            }, function (response) {

            });
        }
        //like
        $scope.likeBook = function (book) {
            /**
             * sending like request as "post"
             * handled on the servlet:
             *  if like is sent - it's removed from this books likes
             *  else, if user has purchased this book, a like is set and added to book likes
             */

            // sending like request as post.
            $http.post("LikeServlet/book/" + book.IdBook).then(function (response) {
                // upon success, update this book's likes list
                getLikesForBook(book);
            }, function (response) {

            });

        };

        // buy book


        // $scope.userBoughtThisBook = function (bookname){
        //     if ($scope.bookUserData.isLogged === false)
        //         return false;
        //     if (bookname in $scope.purchasedBooklist){ // user bought it
        //         return true;
        //     } else
        //         return false;
        //    // $filter('filter')(myArray, {'id':73})
        //    // if $scope.book.Name
        // };
    }
]);

bookApp.controller('loginController', ['$scope', '$http', 'userData', function ($scope, $http, userData) {

    $scope.loginUserData = userData; //isLogged, loggedUser, adminLogged

    $scope.loginMode = false;
    $scope.registerMode = false;
    $scope.loginFormErrorToggle = false;
    //userData.loggedUser = "";
    $scope.loginUserData.loggedUser = "";
    //$scope.loggedUser = userData.loggedUser;
    //$scope.adminLogged = false;
    //$scope.adminLogged = userData.adminLogged;
    $scope.loginUserData.adminLogged = false;


    $scope.loginUserData.isLogged = isLoggedFunc();

    function isLoggedFunc() {
        $http.get("LoginServlet").then(function (response) {
                $scope.loginUserData.isLogged = true;
                $scope.loginUserData.loggedUser = response.data.username;
                if ("isAdmin" in response.data) {
                    $scope.loginUserData.adminLogged = true;
                    $scope.loginUserData.getNewReviewsFunc();
                }
                $scope.loginUserData.getPurchasedBooksFunc(); // TODO: check
            },
            function (response) {});
    }

    $scope.logoutFunc = function () {
        $http.post("LogoutServlet").then(function () {
            $scope.loginUserData.isLogged = false;
            $scope.loginUserData.loggedUser = "";
            $scope.loginUserData.adminMode(0);
            $scope.loginUserData.adminLogged = false;
            $scope.loginUserData.purchasedBooks = [];
            console.log("logout successful");
        }, function () {
            console.log("logout failed");
        });
    };

    $scope.loginFunc = function () {
        if ($scope.loginForm.$invalid) {
            return;
        }
        var obj = {
            Username: $scope.login.User,
            Password: $scope.login.Password
        };

        $http.post("LoginServlet", obj).then(function (response) {
            $scope.loginFormErrorToggle = false;
            // $scope.loginUsernameErrorToggle = false;
            // $scope.loginUsernameError = "";
            // $scope.loginPasswordErrorToggle = false;
            // $scope.loginPasswordError = "";
            $scope.loginUserData.loggedUser = $scope.login.User;
            $scope.loginUserData.isLogged = true;
            if (angular.equals(obj.Username, "admin")) {
                // admin login successful
                $scope.loginUserData.adminLogged = true;
                $scope.loginUserData.getNewReviewsFunc();
            }
            // upon succsess
            $scope.loginUserData.getPurchasedBooksFunc(); // TODO: check
            $scope.loginMode = false;
        }, function (reposnse) {
            $scope.loginFormErrorToggle = true;
            $scope.loginFormError = "Wrong username or password";
            $scope.loginUserData.loggedUser = "";
            $scope.loginUserData.isLogged = false;
            return;
        });
    };

    $scope.registerFunc = function () {
        console.log("do register");
        if ($scope.registerForm.$invalid) {
            return;
        }
        var obj = {
            Username: $scope.register.User,
            Password: $scope.register.Password,
            Nickname: $scope.register.NickName,
            Email: $scope.register.Email,
            Phone: $scope.register.Telephone.Area + "" + $scope.register.Telephone.Full,
            Address: $scope.register.Address.Street + " " +
                $scope.register.Address.City + " " +
                $scope.register.Address.zip,
            Description: $scope.register.About,
            Photo: $scope.register.photo
        };

        $http.post("RegistrationServlet", obj).then(function (response) {
            $scope.RegisterFormErrorToggle = false;

            $scope.loginUserData.loggedUser = $scope.register.User;
            $scope.loginUserData.isLogged = true;
            $scope.registerMode = false;

            // upon success
            $scope.loginMode = false;
        }, function (reposnse) {
            $scope.RegisterFormErrorToggle = true;
            $scope.registerFormError = "Error filling form";
            $scope.loginUserData.loggedUser = "";
            $scope.loginUserData.isLogged = false;
            return;
        });
        //upon success
    };
    $scope.openLogin = function () {
        $scope.loginMode = !$scope.loginMode;
    };
    $scope.openRegister = function () {
        $scope.registerMode = !$scope.registerMode;
        console.log("register mode active");
    };
}]);

bookApp.controller('adminController', ['$scope', '$http', '$filter', 'userData',
    function ($scope, $http, $filter, userData) {
        $scope.AdminViews = {
            adminMenuView: false,
            usersView: false,
            reviewsView: false,
            transactionsView: false,
            whatToShow: "",
            show: function (whatToShow) {
                if (userData.adminLogged !== true)
                    return;
                $scope.AdminViews.whatToShow = whatToShow;
                switch (whatToShow) {
                    case "reviews":
                        getReviews();
                        break;
                    case "users":
                        getUsers();
                        break;
                    case "transactions":
                        getTransactions();
                        break;
                    case "books":
                        //show regular books
                        break;
                    default:
                        console.log("wrong AdminView");
                }
            }
        };
        // userData; //isLogged, loggedUser, adminLogged
        userData.adminMode = function adminModeFunc(reviewsMode) {
            if (userData.adminLogged !== true)
                return;
            if ($scope.adminMode === true) {
                $scope.adminMode = !$scope.adminMode;
            } else {
                $scope.adminMode = true;
                if (reviewsMode == 1) {
                    $scope.AdminViews.show('reviews');
                }
            }
            $scope.AdminViews.adminMenuView = true;
            // getTransactions();
            // getUsers();
            // getReviews();
        };

        $scope.adminTransactions = {
            show: false,
            Transactions: null
        };
        $scope.adminUser = {
            show: false,
            users: {},
            deleteUser: function (userToDelete) {
                if (userData.adminLogged !== true)
                    return;
                var obj = {
                    username: userToDelete
                };
                if (!confirm("Are you sure?")) {
                    return;
                }
                // $http.delete("CustomersServlet/name/"+userToDelete, obj).then(function (response) {
                $http.delete("customers/name/" + userToDelete, obj).then(function (response) {
                    console.log("deleted user " + userToDelete);
                    getUsers();
                    // deleted
                }, function (reposnse) {
                    // failed to delete
                    return;
                });
            }
        };
        $scope.adminReview = {
            show: false,
            Reviews: {},
            approveReview: function (reviewIdtoApprove) {
                if (userData.adminLogged !== true)
                    return;
                // var obj = {
                //     reviewID: reviewIdtoApprove
                // };
                // $http.delete("CustomersServlet/name/"+userToDelete, obj).then(function (response) {
                $http.put("ReviewServlet/review/" + reviewIdtoApprove).then(function (response) {
                    console.log("approved review " + reviewIdtoApprove);
                    getReviews();
                    userData.getAllBooksFunc();
                    // Approved
                }, function (reposnse) {
                    // failed to Approved
                    return;
                });
            }
        };

        function getTransactions() {
            $http.get("Purchase").then(function (response) {
                $scope.adminTransactions.Transactions = response.data;
            }, function (reposnse) {
                // failed getting transactions
                return;
            });
        }

        function getUsers() {
            $http.get("customers").then(function (response) {
                $scope.adminUser.users = response.data;
            }, function (reposnse) {
                // failed gettin transactions
                return;
            });
        }

        var getReviews = function getReviews() {
            $http.get("ReviewServlet/new").then(function (response) {
                $scope.adminReview.Reviews = response.data;
                userData.newReviews = response.data.length;
            }, function (reposnse) {
                // failed gettin transactions
                return;
            });
        };
        userData.getNewReviewsFunc = getReviews;

        $scope.getBooknameById = function (bookId) {
            return userData.booklistnames[bookId - 1];
        };

        //admin controller
    }
]);


// jQuery stuff

//support for tooltips (from w3schools)
// $(document).ready(function(){
//     $('[data-toggle="tooltip"]').tooltip();
// });
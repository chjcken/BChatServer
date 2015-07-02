namespace cpp bchatService
namespace java com.bchat.service

	//some struct here
	struct Account {
		1:string id
		2:string password
		3:string name
		4:string email
	}

	struct Request {
		1:i32 requestCode;
		2:Account sender;
		3:string receiver;
		4:string message;

	}

	service Chatter {

	//login
	bool login(1:string id, 2:string password);

	//get friend list
	list<Account> getFriendList(1:string userid);

	//send request
	bool sendRequest(1:Request request);

	//get request	
	list<Request> getRequest(1:string userid);


	}

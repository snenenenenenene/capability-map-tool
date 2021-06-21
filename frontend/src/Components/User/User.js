import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import MaterialTable from "material-table";
import API from "../../Services/API";
export default class User extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      users: [],
    };
  }

  async componentDidMount() {
    this.state.api.createEntity({ name: "user" });
    this.state.api.createEntity({ name: "role" });
    await this.state.api.endpoints.user
      .getAll()
      .then((response) => this.setState({ users: response.data }))
      .catch((error) => {
        toast.error("No Users Were Found");
      });
  }
  //REDIRECT TO EDIT PAGE
  edit(userId) {
    this.props.history.push(`/user/${userId}`);
  }

  //DELETE USER
  fetchDeleteUsers = async (userId) => {
    await this.state.api.endpoints.user
      .delete({ id: userId })
      .then((response) => toast.success("Successfully Deleted User"))
      .catch((error) => toast.error("Could not Delete User"));
    //REFRESH USERS
    await this.state.api.endpoints.user
      .getAll()
      .then((response) => {
        this.setState({ users: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Users");
      });
  };
  //ASK ADMIN WHETHER THEY ARE SURE ABOUT REMOVING A USER
  delete = async (userId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this User?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteUsers(userId);
              }}
            >
              Yes!
            </button>
            <button
              className="btn btn-secondary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => toast.dismiss(t.id)}
            >
              No!
            </button>
          </div>
        </span>
      ),
      { duration: 50000 }
    );
  };

  render() {
    return (
      <div className="container">
        <br></br>
        <nav aria-label="breadcrumb shadow">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/`}>Home</Link>
            </li>
            <li className="breadcrumb-item">Users</li>
          </ol>
        </nav>
        <MaterialTable
          title="Users"
          actions={[
            {
              icon: "add",
              tooltip: "Add User",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(`/user/add`);
              },
            },
          ]}
          columns={[
            { title: "ID", field: "userId" },
            { title: "Name", field: "username" },
            { title: "Email", field: "email" },
            { title: "Role", field: "roleDto.roleName" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.userId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.userId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.users}
        />
      </div>
    );
  }
}

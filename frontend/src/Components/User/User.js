import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import MaterialTable from "material-table";
import axios from "axios";

export default class User extends Component {
  constructor(props) {
    super(props);
    this.state = {
      users: [],
    };
  }

  async componentDidMount() {
    await axios
      .get(`${process.env.REACT_APP_API_URL}/user/`)
      .then((response) => this.setState({ users: response.data }))
      .catch((error) => {
        toast.error("No Users Were Found");
      });
  }

  edit(userId) {
    this.props.history.push(`/user/${userId}`);
  }

  fetchDeleteUsers = async (userId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/user/${userId}`)
      .then((response) => toast.success("Succesfully Deleted Capability"))
      .catch((error) => toast.error("Could not Delete Capability"));
    //REFRESH CAPABILITIES
    await axios
      .get(`${process.env.REACT_APP_API_URL}/user/${userId}`)
      .then((response) => {
        this.setState({ users: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Users");
      });
  };

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
      <div>
        <br></br>
        <nav aria-label="breadcrumb shadow">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/`}>Home</Link>
            </li>
            <li className="breadcrumb-item">Users</li>
          </ol>
        </nav>
        <div className="jumbotron shadow">
          <div>
            <h1 className="display-4" style={{ display: "inline-block" }}>
              Users
            </h1>
            <Link to={`/user/add`}>
              <button className="btn btn-primary float-right">Add User</button>
            </Link>
          </div>
          <br />
          <br />
          <MaterialTable
            columns={[
              { title: "ID", field: "userId" },
              { title: "Name", field: "username" },
              { title: "Email", field: "email" },
              { title: "Role", field: "roleId" },
              {
                title: "",
                name: "delete",
                render: (rowData) => (
                  <button className="btn btn-secondary">
                    <i
                      onClick={this.delete.bind(this, rowData.userId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                ),
              },
              {
                title: "",
                name: "edit",
                render: (rowData) => (
                  <button className="btn btn-secondary">
                    <i
                      onClick={this.edit.bind(this, rowData.userId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                ),
              },
            ]}
            data={this.state.users}
            options={{
              showTitle: false,
            }}
          />
        </div>
      </div>
    );
  }
}

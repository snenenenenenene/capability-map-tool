import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import toast from "react-hot-toast";
import axios from "axios";

export default class ITApplication extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      itApplications: [],
    };
  }

  async componentDidMount() {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/itapplication/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ itApplications: response.data });
      })
      .catch((error) => {
        toast.error("No IT Applications Found");
      });
  }

  edit(itApplicationId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/itapplication/${itApplicationId}`
    );
  }

  fetchDeleteITApplications = async (itApplicationId) => {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .delete(
        `${process.env.REACT_APP_API_URL}/itapplication/${itApplicationId}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) => toast.success("Succesfully Deleted IT Application"))
      .catch((error) => toast.error("Could not Delete IT Application"));
    //REFRESH CAPABILITIES
    await axios
      .get(`${process.env.REACT_APP_API_URL}/itapplication/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ itApplications: [] });
        this.setState({ itApplications: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find IT Applications");
      });
  };

  //DELETE CAPABILITY AND REMOVE ALL CHILD CAPABILITIES FROM STATE
  delete = async (itApplicationId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this IT Application?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteITApplications(itApplicationId);
              }}
            >
              Yes!
            </button>
            <button
              className='btn btn-secondary btn-sm m-3'
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
      <div className='container'>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className='breadcrumb-item'>IT Applications</li>
          </ol>
        </nav>
        <MaterialTable
          title='IT Applications'
          actions={[
            {
              icon: "add",
              tooltip: "Add IT Application",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/itapplication/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "itApplicationId" },
            { title: "Name", field: "name" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.itApplicationId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.itApplicationId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={() => this.handleItemModal()}
                      className='bi bi-app-indicator'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.itApplications}
        />
      </div>
    );
  }
}

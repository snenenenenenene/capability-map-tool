import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";
import toast from "react-hot-toast";

export default class Resource extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      resources: [],
      reload: false,
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
        console.log(error);
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/resource/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ resources: response.data });
      })
      .catch((error) => {
        console.log(error);
        // this.props.history.push('/error')
      });
  }

  edit(resourceId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/resource/${resourceId}`
    );
  }
  fetchDeleteResources = async (resourceId) => {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .delete(`${process.env.REACT_APP_API_URL}/resource/${resourceId}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => toast.success("Successfully Deleted Resource"))
      .catch((error) => toast.error("Could not Delete Resource"));
    //REFRESH RESOURCES
    await axios
      .get(`${process.env.REACT_APP_API_URL}/resource/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Resources");
      });
  };

  delete = async (resourceId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this resource?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteResources(resourceId);
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
            <li className='breadcrumb-item'>Resources</li>
          </ol>
        </nav>
        <MaterialTable
          title='Resources'
          actions={[
            {
              icon: "add",
              tooltip: "Add Resource",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/resource/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "resourceId" },
            { title: "Name", field: "resourceName" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.resourceId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.resourceId)}
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
          data={this.state.resources}
        />
      </div>
    );
  }
}

import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";

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
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        console.log(error);
        this.props.history.push("/error");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/resource/`)
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
  //DELETE resource AND REMOVE ALL CHILD resources FROM STATE
  delete = async (resourceId) => {
    if (window.confirm("Are you sure you want to delete this resource?")) {
      await axios
        .delete(`${process.env.REACT_APP_API_URL}/resource/${resourceId}`)
        .catch((error) => console.error(error));
      //REFRESH resources
      await axios
        .get(`${process.env.REACT_APP_API_URL}/resource/`)
        .then((response) => {
          this.setState({ resources: [] });
          this.setState({ resources: response.data });
        })
        .catch((error) => {
          console.log(error);
          this.props.history.push("/error");
        });
    }
  };

  render() {
    return (
      <div>
        <br></br>
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/`}>Home</Link>
            </li>
            <li className="breadcrumb-item">
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className="breadcrumb-item">Resources</li>
          </ol>
        </nav>
        <div className="jumbotron">
          <div>
            <h1 className="display-4" style={{ display: "inline-block" }}>
              Resources
            </h1>
            <Link
              to={`/environment/${this.state.environmentName}/resource/add`}
            >
              <button className="btn btn-primary float-right">
                Add Resource
              </button>
            </Link>
          </div>
          <br />
          <br />
          <MaterialTable
            columns={[
              { title: "ID", field: "resourceId" },
              { title: "Name", field: "resourceName" },
              {
                title: "",
                name: "delete",
                render: (rowData) => (
                  <button className="btn btn-secondary">
                    <i
                      onClick={this.delete.bind(this, rowData.resourceId)}
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
                      onClick={this.edit.bind(this, rowData.resourceId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                ),
              },
            ]}
            data={this.state.resources}
            options={{
              showTitle: false,
              search: false,
            }}
          />
        </div>
      </div>
    );
  }
}

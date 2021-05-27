import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";

export default class Status extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      statuses: [],
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
      .get(`${process.env.REACT_APP_API_URL}/status/`)
      .then((response) => this.setState({ statuses: response.data }))
      .catch((error) => {
        console.log(error);
        // this.props.history.push('/error')
      });
  }

  edit(id) {
    console.log("edit");
    this.props.history.push(
      `/environment/${this.state.environmentName}/status/${id}`
    );
  }

  delete = async (statusId) => {
    if (window.confirm("Are you sure you want to delete this status?")) {
      await axios
        .delete(`${process.env.REACT_APP_API_URL}/status/${statusId}`)
        .catch((error) => {
          if (error.response.status === 500)
            alert(
              "This status is in use by a capability and could not be deleted!"
            );
          console.error(error);
        });
      //REFRESH CAPABILITIES

      await axios
        .get(`${process.env.REACT_APP_API_URL}/status/`)
        .then((response) => {
          console.log(response.data);
          this.setState({ statuses: response.data });
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
            <li className="breadcrumb-item">Statuses</li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h1 style={{ display: "inline-block" }} className="display-4">
            Statuses
          </h1>
          <Link to={`/environment/${this.state.environmentName}/status/add`}>
            <button className="btn btn-primary float-right">Add Status</button>
          </Link>
          <br />
          <br />
          <MaterialTable
            columns={[
              { title: "ID", field: "statusId" },
              { title: "Date", field: "validityPeriod" },
              {
                title: "",
                name: "delete",
                render: (rowData) => (
                  <button className="btn btn-secondary">
                    <i
                      onClick={this.delete.bind(this, rowData.statusId)}
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
                      onClick={this.edit.bind(this, rowData.statusId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                ),
              },
            ]}
            data={this.state.statuses}
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

import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";
import toast from "react-hot-toast";

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
        this.props.history.push("/404");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        console.log(error);
        this.props.history.push("/404");
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

  fetchDeleteStatuses = async (statusId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/status/${statusId}`)
      .then((response) => toast.success("Successfully Deleted Status"))
      .catch((error) => toast.error("Could not Delete Status"));
    //REFRESH STATUSES
    await axios
      .get(`${process.env.REACT_APP_API_URL}/status/`)
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Statuses");
      });
  };

  delete = async (statusId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this Status?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteStatuses(statusId);
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
      <div>
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
            <li className='breadcrumb-item'>Statuses</li>
          </ol>
        </nav>
        <MaterialTable
          title='Statuses'
          actions={[
            {
              icon: "add",
              tooltip: "Add Status",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/status/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "statusId" },
            { title: "Date", field: "validityPeriod" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.statusId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.statusId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.statuses}
        />
      </div>
    );
  }
}

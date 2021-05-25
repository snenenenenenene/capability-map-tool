import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import "./GeneralTable.css";
import axios from "axios";
import toast from "react-hot-toast";

export default class Capability extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      capabilities: [],
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
        this.props.history.push("/notfounderror");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`
      )
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        console.log(error);
        toast.error("Could Not Find Capabilities");
      });
  }

  edit(capabilityId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/capability/${capabilityId}`
    );
  }

  fetchDeleteCapabilities = async (capabilityId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/capability/${capabilityId}`)
      .then((response) => toast.success("Succesfully Deleted Capability"))
      .catch((error) => toast.error("Could not Delete Capability"));
    //REFRESH CAPABILITIES
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`
      )
      .then((response) => {
        this.setState({ capabilities: [] });
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Capabilities");
      });
  };

  delete = async (capabilityId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this capability?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteCapabilities(capabilityId);
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
            <li className='breadcrumb-item'>Capability</li>
          </ol>
        </nav>
        <div className='jumbotron'>
          <div>
            <h1 className='display-4' style={{ display: "inline-block" }}>
              Capabilities
            </h1>
            <Link
              to={`/environment/${this.state.environmentName}/capability/add`}
            >
              <button className='btn btn-primary float-right'>
                Add Capability
              </button>
            </Link>
          </div>
          <br />
          <br />
          <MaterialTable
            columns={[
              { title: "ID", field: "capabilityId" },
              { title: "Name", field: "capabilityName" },
              { title: "Parent ID", field: "parentCapabilityId" },
              { title: "Level", field: "level" },
              { title: "Expiration", field: "status.validityPeriod" },
              {
                title: "",
                name: "delete",
                render: (rowData) => (
                  <button className='btn btn-secondary'>
                    <i
                      onClick={this.delete.bind(this, rowData.capabilityId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                ),
              },
              {
                title: "",
                name: "edit",
                render: (rowData) => (
                  <button className='btn btn-secondary'>
                    <i
                      onClick={this.edit.bind(this, rowData.capabilityId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                ),
              },
            ]}
            data={this.state.capabilities}
            parentChildData={(row, rows) =>
              rows.find((a) => a.capabilityId === row.parentCapabilityId)
            }
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

import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";
import toast from "react-hot-toast";
export default class Program extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      programs: [],
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
      .get(`${process.env.REACT_APP_API_URL}/program/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ programs: response.data });
      })
      .catch((error) => {
        toast.error("Could not Load Programs");
      });
  }

  edit(programId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/program/${programId}`
    );
  }
  fetchDeleteCapabilities = async (programId) => {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .delete(`${process.env.REACT_APP_API_URL}/program/${programId}`)
      .then((response) => toast.success("Successfully Deleted Program"))
      .catch((error) => toast.error("Could not Delete Program"));
    await axios
      .get(`${process.env.REACT_APP_API_URL}/program/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Programs");
      });
  };

  delete = async (programId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this program?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeletePrograms(programId);
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
            <li className='breadcrumb-item'>Programs</li>
          </ol>
        </nav>
        <MaterialTable
          title='Programs'
          actions={[
            {
              icon: "add",
              tooltip: "Add Program",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/program/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "programId" },
            { title: "Name", field: "programName" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.programId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.programId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.programs}
        />
      </div>
    );
  }
}

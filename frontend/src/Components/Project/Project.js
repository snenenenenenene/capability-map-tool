import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";
import toast from "react-hot-toast";

export default class Project extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      projects: [],
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
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/project/`)
      .then((response) => {
        this.setState({ projects: response.data });
      })
      .catch((error) => {
        console.log(error);
        // this.props.history.push('/error')
      });
  }

  edit(projectId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/project/${projectId}`
    );
  }
  //DELETE project AND REMOVE ALL CHILD projects FROM STATE
  delete = async (projectId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this project?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteProjects(projectId);
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

  fetchDeleteProjects = async (projectId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/project/${projectId}`)
      .then((response) => toast.success("Successfully Deleted Project"))
      .catch((error) => toast.error("Could not Delete Project"));
    //REFRESH CAPABILITIES
    await axios
      .get(`${process.env.REACT_APP_API_URL}/project/`)
      .then((response) => {
        this.setState({ projects: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Projects");
      });
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
            <li className='breadcrumb-item'>Projects</li>
          </ol>
        </nav>
        <MaterialTable
          title='Projects'
          actions={[
            {
              icon: "add",
              tooltip: "Add Project",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/project/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "projectId" },
            { title: "Name", field: "projectName" },
            { title: "Program", field: "program.programName" },
            { title: "Status", field: "status.validityPeriod" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.projectId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.projectId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.projects}
        />
      </div>
    );
  }
}

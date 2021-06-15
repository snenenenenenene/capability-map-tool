import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import toast from "react-hot-toast";
import API from "../../Services/API";
import { Modal } from "react-bootstrap";
import Select from "react-select";

export default class Project extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      projectId: "",
      projects: [],
      capabilities: [],
      linkedCapabilities: [],
      showModal: false,
    };
  }

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "capability" });
    this.state.api.createEntity({ name: "program" });
    this.state.api.createEntity({ name: "status" });
    this.state.api.createEntity({ name: "project" });
    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) => {
        this.setState({ environmentId: response.data.environmentId });
      })
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.project
      .getAll()
      .then((response) => {
        this.setState({ projects: response.data });
      })
      .catch((error) => {
        this.props.history.push("/error");
      });
    await this.state.api.endpoints.capability
      .getAll()
      .then((response) => {
        response.data.forEach((capability) => {
          capability.label = capability.capabilityName;
          capability.value = capability.capabilityId;
        });
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        this.props.history.push("/error");
      });
  }

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  handleSubmit = (projectId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("projectId", projectId);
    formData.append("capabilityId", this.state.capabilityId);
    await this.state.api.endpoints.capability
      .linkProject(formData)
      .then(toast.success("Project Successfully Linked"))
      .catch((error) => toast.error("Could not link Project"));

    this.capabilityTable(projectId);
  };

  edit(projectId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/project/${projectId}`
    );
  }

  async unlinkCapability(capabilityId) {
    await this.state.api.endpoints.capability
      .unlinkProject({ capabilityId: capabilityId, id: this.state.projectId })
      .then(toast.success("Link Successfully Deleted"))
      .catch((error) => toast.error("Could not Unlink"));

    this.capabilityTable(this.state.projectId);
  }
  //DELETE PROJECT
  delete = async (projectId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this project?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteProjects(projectId);
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

  async capabilityTable(projectId) {
    await this.state.api.endpoints.project
      .getCapabilities({ id: projectId })
      .then((response) => {
        this.setState({ linkedCapabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  }

  fetchDeleteProjects = async (projectId) => {
    await this.state.api.endpoints.project
      .delete({ id: projectId })
      .then((response) => toast.success("Successfully Deleted Project"))
      .catch((error) => toast.error("Could not Delete Project"));
    //REFRESH PROJECTS
    await this.state.api.endpoints.project
      .getAll()
      .then((response) => {
        this.setState({ projects: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Projects");
      });
  };

  render() {
    return (
      <div className="container">
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
            <li className="breadcrumb-item">Projects</li>
          </ol>
        </nav>
        <MaterialTable
          title="Projects"
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
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.projectId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.projectId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={() => this.handleModal()}
                      className="bi bi-chat-square"
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.projects}
          detailPanel={(rowData) => {
            return (
              <div>
                <div className="card-deck" style={{ padding: 10, margin: 5 }}>
                  {this.state.linkedCapabilities.map((capability) => {
                    return (
                      <div
                        className="card"
                        style={{
                          margin: 3,
                          maxWidth: 120,
                          maxHeight: 120,
                        }}
                      >
                        <div className="strategyitem-title card-header text-center text-uppercase text-truncate">
                          {capability.capabilityName}
                        </div>
                        <div
                          className="card-body text-center"
                          style={{ padding: 5 }}
                        >
                          <button
                            className="btn btn-danger"
                            onClick={() =>
                              this.unlinkCapability(capability.capabilityId)
                            }
                          >
                            UNLINK
                          </button>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            );
          }}
          onRowClick={(event, rowData, togglePanel) => {
            this.setState({ projectId: rowData.projectId });
            this.capabilityTable(rowData.projectId);
            togglePanel();
          }}
        />
        <Modal show={this.state.showModal} onHide={() => this.handleModal()}>
          <Modal.Header closeButton>
            Link Project {this.state.projectId}
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.handleSubmit(this.state.projectId)}>
              <label htmlFor="capabilityId">Capability</label>
              <Select
                options={this.state.capabilities}
                noOptionsMessage={() => "No Capabilities"}
                onChange={(capability) => {
                  if (capability) {
                    this.setState({
                      capabilityId: capability.capabilityId,
                    });
                  } else {
                    this.setState({ capabilityId: 0 });
                  }
                }}
                placeholder="Optional"
              />
              <br></br>
              <button className="btn btn-primary" type="sumbit">
                SUBMIT
              </button>
            </form>
          </Modal.Body>
        </Modal>
      </div>
    );
  }
}

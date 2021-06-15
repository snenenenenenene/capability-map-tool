import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import toast from "react-hot-toast";
import API from "../../Services/API";
import { Modal } from "react-bootstrap";
import Select from "react-select";

export default class Resource extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      resources: [],
      capabilities: [],
      resourceId: "",
      showModal: false,
      linkedCapabilities: [],
    };
  }

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "capability" });
    this.state.api.createEntity({ name: "resource" });

    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({
          environmentId: response.data.environmentId,
        })
      )
      .catch((error) => {
        this.props.history.push("/home");
      });

    await this.state.api.endpoints.resource
      .getAll()
      .then((response) => {
        this.setState({ resources: response.data });
      })
      .catch((error) => {
        toast.error("Could not Load Resources");
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

  //REDIRECT TO EDIT PAGE
  edit(resourceId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/resource/${resourceId}`
    );
  }

  //CONFIRM RESOURCE DELETION WITH ID RESOURCEID
  fetchDeleteResources = async (resourceId) => {
    await this.state.api.endpoints.resource
      .delete({ id: resourceId })
      .then((response) => toast.success("Successfully Deleted Resource"))
      .catch((error) => toast.error("Could not Delete Resource"));
    //REFRESH RESOURCES
    await this.state.api.endpoints.resource
      .getAll()
      .then((response) => {
        this.setState({ resources: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Resources");
      });
  };

  //DELETE RESOURCE
  delete = async (resourceId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this resource?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteResources(resourceId);
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

  //UNLINK CAPABILITY FROM RESOURCE
  async unlinkCapability(capabilityId) {
    await this.state.api.endpoints.capability
      .unlinkResource({ capabilityId: capabilityId, id: this.state.resourceId })
      .then(toast.success("Link Successfully Deleted"))
      .catch((error) => toast.error("Could not Unlink"));

    this.capabilityTable(this.state.resourceId);
  }

  //GENERATE CAPABILITY TABLE WITH ALL LINKED CAPABILTIES TO A RESOURCE
  async capabilityTable(resourceId) {
    await this.state.api.endpoints.resource
      .getCapabilities({ id: resourceId })
      .then((response) => {
        this.setState({ linkedCapabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  }

  //TOGGLE MODAL
  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  //HANDLE SUBMIT
  handleSubmit = (resourceId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("resourceId", resourceId);
    formData.append("capabilityId", this.state.capabilityId);
    await this.state.api.endpoints.capability
      .linkResource(formData)
      .then(toast.success("Resource Successfully Linked"))
      .catch((error) => toast.error("Could not link Resource"));
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
            <li className="breadcrumb-item">Resources</li>
          </ol>
        </nav>
        <MaterialTable
          title="Resources"
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
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.resourceId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.resourceId)}
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
          data={this.state.resources}
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
            this.setState({ resourceId: rowData.resourceId });
            this.capabilityTable(rowData.resourceId);
            togglePanel();
          }}
        />
        <Modal show={this.state.showModal} onHide={() => this.handleModal()}>
          <Modal.Header closeButton>
            Link Resource {this.state.resourceId}
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.handleSubmit(this.state.resourceId)}>
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

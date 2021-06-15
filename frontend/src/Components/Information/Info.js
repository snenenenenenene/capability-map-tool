import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import toast from "react-hot-toast";
import API from "../../Services/API";
import { Modal } from "react-bootstrap";
import Select from "react-select";

export default class Info extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      informationId: "",
      criticality: "",
      infos: [],
      capabilities: [],
      linkedCapabilities: [],
      showModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "capability" });
    this.state.api.createEntity({ name: "information" });
    this.state.api.createEntity({ name: "capabilityinformation" });
    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) => {
        this.setState({ environmentId: response.data.environmentId });
      })
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.information
      .getAll()
      .then((response) => {
        this.setState({ infos: response.data });
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

  handleSubmit = (informationId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("informationId", this.state.informationId);
    formData.append("capabilityId", this.state.capabilityId);
    formData.append("criticality", this.state.criticality);
    await this.state.api.endpoints.capabilityinformation
      .create(formData)
      .then(toast.success("Info Successfully Linked"))
      .catch((error) => toast.error("Could not link Info"));

    this.capabilityTable(informationId);
  };

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  edit(informationId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/info/${informationId}`
    );
  }

  async unlinkCapability(capabilityId) {
    await this.state.api.endpoints.capabilityinformation
      .unlink({ capabilityId: capabilityId, id: this.state.informationId })
      .then(toast.success("Link Successfully Deleted"))
      .catch((error) => toast.error("Could not Unlink"));

    this.capabilityTable(this.state.informationId);
  }

  //DELETE info
  delete = async (infoId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this info?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteInfos(infoId);
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

  async capabilityTable(informationId) {
    await this.state.api.endpoints.capabilityinformation
      .getAllCapabilitiesByInformationId({ id: informationId })
      .then((response) => {
        this.setState({ linkedCapabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  }

  fetchDeleteInfos = async (infoId) => {
    await this.state.api.endpoints.info
      .delete({ id: infoId })
      .then((response) => toast.success("Successfully Deleted Info"))
      .catch((error) => toast.error("Could not Delete Info"));
    //REFRESH INFO
    await this.state.api.endpoints.information
      .getAll()
      .then((response) => {
        this.setState({ infos: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Infos");
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
            <li className="breadcrumb-item">Infos</li>
          </ol>
        </nav>
        <MaterialTable
          title="Infos"
          actions={[
            {
              icon: "add",
              tooltip: "Add Info",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/info/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "informationId" },
            { title: "Name", field: "informationName" },
            { title: "Description", field: "informationDescription" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.informationId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.informationId)}
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
          data={this.state.infos}
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
                          {capability.capability.capabilityName}
                        </div>
                        <div
                          className="card-body text-center"
                          style={{ padding: 5 }}
                        >
                          <button
                            className="btn btn-danger"
                            onClick={() =>
                              this.unlinkCapability(
                                capability.capability.capabilityId
                              )
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
            this.setState({ informationId: rowData.informationId });
            this.capabilityTable(rowData.informationId);
            togglePanel();
          }}
        />
        <Modal show={this.state.showModal} onHide={() => this.handleModal()}>
          <Modal.Header closeButton>
            Link Info {this.state.informationId}
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.handleSubmit(this.state.informationId)}>
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
              <label htmlFor="criticality">Criticality</label>
              <select
                className="form-control"
                name="criticality"
                id="criticality"
                placeholder="Add Criticality"
                onChange={this.handleInputChange}
              >
                <option value="NONE">None</option>
                <option value="LOWEST">Lowest</option>
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
                <option value="HIGHEST">Highest</option>
              </select>
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

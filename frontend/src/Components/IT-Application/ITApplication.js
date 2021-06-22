import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import toast from "react-hot-toast";
import { Modal } from "react-bootstrap";
import Select from "react-select";
import ReactStars from "react-stars";
import API from "../../Services/API";

export default class ITApplication extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      itApplicationId: "",
      capabilityId: "",
      itApplications: [],
      capabilities: [],
      currencies: [],
      linkedCapabilities: [],
      showModal: false,

      // RATINGS
      availability: 2,
      efficiencySupport: 2,
      functionalCoverage: 2,
      correctnessBusinessFit: 2,
      futurePotential: 2,
      completeness: 2,
      correctnessInformationFit: 2,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }
  //HANDLE INPUT CHANGE
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  //UNLINK CAPABILITIES FROM ITAPPLICATION
  async unlinkCapability(capabilityId) {
    await this.state.api.endpoints.capabilityapplication
      .unlink({ capabilityId: capabilityId, id: this.state.itApplicationId })
      .then(toast.success("Link Successfully Deleted"))
      .catch((error) => toast.error("Could not Unlink"));

    this.capabilityTable(this.state.itApplicationId);
  }
  //FETCH CAPABILITIES AND INSERT THEM INTO HTML SELECT
  async capabilityTable(itApplicationId) {
    await this.state.api.endpoints.capabilityapplication
      .getAllCapabilitiesByApplicationId({ id: itApplicationId })
      .then((response) => {
        this.setState({ linkedCapabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  }

  //HANDLE SUBMIT
  handleSubmit = (itApplicationId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("efficiencySupport", this.state.efficiencySupport);
    formData.append("functionalCoverage", this.state.functionalCoverage);
    formData.append(
      "correctnessBusinessFit",
      this.state.correctnessBusinessFit
    );
    formData.append("futurePotential", this.state.futurePotential);
    formData.append("completeness", this.state.completeness);
    formData.append(
      "correctnessInformationFit",
      this.state.correctnessInformationFit
    );
    formData.append("availability", this.state.availability);

    await this.state.api.endpoints.capabilityapplication
      .linkCapabilityApplication({
        capabilityId: this.state.capabilityId,
        itApplicationId: this.state.itApplicationId,
        form: formData,
      })
      .then(toast.success("Application Successfully Linked"))
      .catch((error) => toast.error("Could not Link Application"));

    await this.state.api.endpoints.capabilityapplication
      .getAllCapabilitiesByApplicationId({ id: itApplicationId })
      .then((response) => {
        this.setState({ linkedCapabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capability");
      });
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "itapplication" });
    this.state.api.createEntity({ name: "capability" });
    this.state.api.createEntity({ name: "capabilityapplication" });

    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.capability
      .getCapabilitiesByEnvironment({ id: this.state.environmentId })
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

    await this.state.api.endpoints.itapplication
      .getAll()
      .then((response) => {
        this.setState({ itApplications: response.data });
      })
      .catch((error) => {
        toast.error("No IT Applications Found");
      });
  }

  //REDIRCET TO EDIT PAGE
  edit(itApplicationId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/itapplication/${itApplicationId}`
    );
  }

  //CONFIRM DELETION OF ITAPPLICATION WTIH ID ITAPPLICATIONID
  fetchDeleteITApplications = async (itApplicationId) => {
    await this.state.api.endpoints.itapplication
      .delete({ id: itApplicationId })
      .then((response) => toast.success("Succesfully Deleted IT Application"))
      .catch((error) => toast.error("Could not Delete IT Application"));
    //REFRESH ITAPPLICATIONS
    await this.state.api.endpoints.itapplication
      .getAll()
      .then((response) => {
        this.setState({ itApplications: [] });
        this.setState({ itApplications: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find IT Applications");
      });
  };

  //TOGGLE MODAL
  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  //DELETE ITAPPLICATION
  delete = async (itApplicationId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this IT Application?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteITApplications(itApplicationId);
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
            <li className="breadcrumb-item">IT Applications</li>
          </ol>
        </nav>
        <MaterialTable
          title="IT Applications"
          actions={[
            {
              icon: "add",
              tooltip: "Add IT Application",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/itapplication/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "itApplicationId" },
            { title: "Name", field: "name" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.itApplicationId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.itApplicationId)}
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
          data={this.state.itApplications}
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
            this.setState({ itApplicationId: rowData.itApplicationId });
            this.capabilityTable(rowData.itApplicationId);
            togglePanel();
          }}
        />
        <Modal show={this.state.showModal} onHide={() => this.handleModal()}>
          <Modal.Header closeButton>
            Link Project {this.state.itApplicationId}
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.handleSubmit(this.state.itApplicationId)}>
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
              <div className="row justify-content-center">
                <div className="col-sm-6 justify-content-center text-center">
                  <div className="form-row">
                    <div className="form-group col-md">
                      <label htmlFor="availability">Availability</label>
                      <div style={{ marginLeft: 28 + "%" }}>
                        <ReactStars
                          count={5}
                          onChange={(newValue) => {
                            this.setState({ availability: newValue });
                          }}
                          size={24}
                          half={false}
                          color2={"#ffd700"}
                          value={this.state.availability}
                        />
                      </div>
                    </div>
                  </div>
                  <br></br>
                  <div className="form-row">
                    <div className="form-group col-md">
                      <label htmlFor="efficiencySupport">
                        Efficiency Support
                      </label>
                      <div style={{ marginLeft: 28 + "%" }}>
                        <ReactStars
                          count={5}
                          onChange={(newValue) => {
                            this.setState({ efficiencySupport: newValue });
                          }}
                          size={24}
                          half={false}
                          color2={"#ffd700"}
                          value={this.state.efficiencySupport}
                        />
                      </div>
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group col-md">
                      <label htmlFor="functionalCoverage">
                        Functional Coverage
                      </label>
                      <div style={{ marginLeft: 28 + "%" }}>
                        <ReactStars
                          count={5}
                          onChange={(newValue) => {
                            this.setState({ functionalCoverage: newValue });
                          }}
                          size={24}
                          half={false}
                          color2={"#ffd700"}
                          value={this.state.functionalCoverage}
                        />
                      </div>
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group col-md">
                      <label htmlFor="correctnessBusinessFit">
                        Correctness Business Fit
                      </label>
                      <div style={{ marginLeft: 28 + "%" }}>
                        <ReactStars
                          count={5}
                          onChange={(newValue) => {
                            this.setState({ correctnessBusinessFit: newValue });
                          }}
                          size={24}
                          half={false}
                          color2={"#ffd700"}
                          value={this.state.correctnessBusinessFit}
                        />
                      </div>
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group col-md">
                      <label htmlFor="futurePotential">Future Potential</label>
                      <div style={{ marginLeft: 28 + "%" }}>
                        <ReactStars
                          count={5}
                          onChange={(newValue) => {
                            this.setState({ futurePotential: newValue });
                          }}
                          size={24}
                          half={false}
                          color2={"#ffd700"}
                          value={this.state.futurePotential}
                        />
                      </div>
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group col-md">
                      <label htmlFor="completeness">Completeness</label>
                      <div style={{ marginLeft: 28 + "%" }}>
                        <ReactStars
                          count={5}
                          onChange={(newValue) => {
                            this.setState({ completeness: newValue });
                          }}
                          size={24}
                          half={false}
                          color2={"#ffd700"}
                          value={this.state.completeness}
                        />
                      </div>
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group col-md">
                      <label htmlFor="correctnessInformationFit">
                        Correctness Information Fit
                      </label>
                      <div style={{ marginLeft: 28 + "%" }}>
                        <ReactStars
                          count={5}
                          onChange={(newValue) => {
                            this.setState({
                              correctnessInformationFit: newValue,
                            });
                          }}
                          size={24}
                          half={false}
                          color2={"#ffd700"}
                          value={this.state.correctnessInformationFit}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
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

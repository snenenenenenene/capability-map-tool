import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import "./GeneralTable.css";
import { Modal } from "react-bootstrap";
import Select from "react-select";
import toast from "react-hot-toast";
import API from "../../Services/API";

export default class Capability extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      capabilityId: "",
      capabilities: [],
      strategyItems: [],
      itemId: 0,
      strategicImportance: "",
      capabilityItems: [],
      showItemModal: false,
      showInfoModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  //HANDLE INPUT CHANGE
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  //HANDLE SUBMIT
  handleSubmit = (capabilityId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("itemId", this.state.itemId);
    formData.append("capabilityId", capabilityId);
    formData.append("strategicImportance", this.state.strategicImportance);
    await this.state.api.endpoints.capabilityitem
      .create(formData)
      .then(toast.success("Item Successfully Added"))
      .catch((error) => toast.error("Could not add Item"));

    await this.state.api.endpoints.capabilityitem
      .getAllCapabilityItemsByCapabilityId({ id: capabilityId })
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
      });
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "capability" });
    this.state.api.createEntity({ name: "strategyitem" });
    this.state.api.createEntity({ name: "capabilityitem" });

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

    await this.state.api.endpoints.capability
      .getAllCapabilitiesByEnvironmentId({ id: this.state.environmentId })
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });

    await this.state.api.endpoints.strategyitem
      .getAll()
      .then((response) => {
        response.data.forEach((item) => {
          item.label = item.strategyItemName;
          item.value = item.itemId;
        });
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
      });
  }

  //REDIRECT TO EDIT PAGE
  edit(capabilityId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/capability/${capabilityId}`
    );
  }

  // FETCH STRATEGY ITEMS AND INSERT THEM INTO HTML SELECT
  async strategyItemTable(capabilityId) {
    await this.state.api.endpoints.capabilityitem
      .getAllCapabilityItemsByCapabilityId({ id: capabilityId })
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
      });
  }

  //TOGGLE ITEM MODAL
  handleItemModal() {
    this.setState({ showItemModal: !this.state.showItemModal });
  }

  //TOGGLE INFO MODAL
  handleInfoModal() {
    this.setState({ showInfoModal: !this.state.showInfoModal });
  }

  //DELETE CAPABILITY
  fetchDeleteCapabilities = async (capabilityId) => {
    await this.state.api.endpoints.capability
      .delete({ id: capabilityId })
      .then((response) => toast.success("Successfully Deleted Capability"))
      .catch((error) => toast.error("Could not Delete Capability"));

    //REFRESH CAPABILITIES
    await this.state.api.endpoints.capability
      .getAllCapabilitiesByEnvironmentId({ id: this.state.environmentId })
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Capabilities");
      });
  };

  //CONFIRM DELETION OF CAPABILTIY WITH ID CAPABILITYID
  delete = async (capabilityId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this capability?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteCapabilities(capabilityId);
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
            <li className="breadcrumb-item">Capability</li>
          </ol>
        </nav>
        <MaterialTable
          title="Capabilities"
          actions={[
            {
              icon: "add",
              tooltip: "Add Capability",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/capability/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "capabilityId" },
            { title: "Name", field: "capabilityName" },
            { title: "Level", field: "level" },
            { title: "Expiration", field: "status.validityPeriod" },
            { title: "PC", field: "paceOfChange" },
            { title: "TOM", field: "targetOperatingModel" },
            { title: "RQ", field: "resourceQuality" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div className="card-deck">
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.capabilityId)}
                      indicator
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.capabilityId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={() => this.handleItemModal()}
                      className="bi bi-app-indicator"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={() => this.handleInfoModal()}
                      className="bi bi-info-square"
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.capabilities}
          parentChildData={(row, rows) =>
            rows.find((a) => a.capabilityId === row.parentCapabilityId)
          }
          detailPanel={(rowData) => {
            return (
              <div>
                <div className="card-deck" style={{ padding: 10, margin: 5 }}>
                  {this.state.capabilityItems.map((capabilityItem) => {
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
                          {capabilityItem.strategyItem.strategyItemName}
                        </div>
                        <div className="card-body text-center">
                          <div className="text-uppercase">
                            {capabilityItem.strategyItem.strategicImportance}
                          </div>
                          {capabilityItem.strategyItem.description}
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            );
          }}
          onRowClick={(event, rowData, togglePanel) => {
            this.setState({ capabilityId: rowData.capabilityId });
            this.strategyItemTable(rowData.capabilityId);
            togglePanel();
          }}
        />
        <Modal
          show={this.state.showItemModal}
          onHide={() => this.handleItemModal()}
        >
          <Modal.Header closeButton>
            <Modal.Title>{this.state.capabilityId}. Add Items</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.handleSubmit(this.state.capabilityId)}>
              <label htmlFor="itemId">Strategy Items</label>
              <Select
                options={this.state.strategyItems}
                noOptionsMessage={() => "No Strategy Items"}
                onChange={(item) => {
                  if (item) {
                    this.setState({
                      itemId: item.itemId,
                    });
                  } else {
                    this.setState({ itemId: 0 });
                  }
                }}
                placeholder="Optional"
              />
              <label htmlFor="strategicImportance">Importance</label>
              <select
                className="form-control"
                name="strategicImportance"
                id="strategicImportance"
                placeholder="Add Importance"
                value={this.state.strategicImportance}
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
        <Modal
          show={this.state.showInfoModal}
          onHide={() => this.handleInfoModal()}
        >
          <Modal.Header closeButton>
            <Modal.Title>{this.state.capabilityId}. Add Info</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.handleSubmit(this.state.capabilityId)}>
              <label htmlFor="itemId">Strategy Items</label>
              <Select
                options={this.state.strategyItems}
                noOptionsMessage={() => "No Strategy Items"}
                onChange={(item) => {
                  if (item) {
                    this.setState({
                      itemId: item.itemId,
                    });
                  } else {
                    this.setState({ itemId: 0 });
                  }
                }}
                placeholder="Optional"
              />
              <label htmlFor="strategicImportance">Importance</label>
              <select
                className="form-control"
                name="strategicImportance"
                id="strategicImportance"
                placeholder="Add Importance"
                value={this.state.strategicImportance}
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

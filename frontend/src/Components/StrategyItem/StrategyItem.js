import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import { Modal } from "react-bootstrap";
import Select from "react-select";
import toast from "react-hot-toast";
import API from "../../Services/API";
export default class StrategyItem extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      strategyItems: [],
      itemId: "",
      capabilityItems: [],
      capabilities: [],
      capabilityId: 0,
      strategicImportance: "",
      showModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "capability" });
    this.state.api.createEntity({ name: "strategyitem" });
    this.state.api.createEntity({ name: "capabilityitem" });

    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.strategyitem
      .getAll()
      .then((response) => {
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
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
        toast.error("Could Not Find Capabilities");
      });
  }

  //REDIRECT TO EDIT STATUS
  edit(strategyItemId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/strategyitem/${strategyItemId}`
    );
  }

  //ASK ADMIN WHETHER THEY ARE SURE ABOUT REMOVING A USER
  fetchDeleteStrategyItems = async (itemId) => {
    await this.state.api.endpoints.strategyitem
      .delete({ id: itemId })
      .then((response) => toast.success("Successfully Deleted Strategy Item"))
      .catch((error) => toast.error("Could not Delete Strategy Item"));
    //REFRESH Strategy Items
    await this.state.api.endpoints.environment
      .getAll()
      .then((response) => {
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Strategy Items");
      });
  };
  //HANDLE INPUT CHANGE
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  //HANDLE SUBMIT
  handleSubmit = (itemId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("itemId", itemId);
    formData.append("capabilityId", this.state.capabilityId);
    formData.append("strategicImportance", this.state.strategicImportance);
    await this.state.api.endpoints.capabilityitem
      .create(formData)
      .then(toast.success("Capability Successfully Added"))
      .catch((error) => toast.error("Could not add Capability"));

    await this.state.api.endpoints.capabilityitem
      .getAllCapabilityItemsByItemId({ id: itemId })
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  };

  //DELETE STRATEGY ITEM
  delete = async (itemId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this Strategy Item?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteStrategyItems(itemId);
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

  //UNLINK CAPABILITY FROM STRATEGY ITEM
  async unlinkCapability(capabilityId) {
    await this.state.api.endpoints.capabilityitem
      .unlink({ capabilityId: capabilityId, id: this.state.itemId })
      .then(toast.success("Link Successfully Deleted"))
      .catch((error) => toast.error("Could not Unlink"));

    this.capabilityTable(this.state.itemId);
  }

  //SHOW ALL CAPABILITIES LINKED TO STRATEGY ITEM WITH ID ITEMID
  async capabilityTable(itemId) {
    await this.state.api.endpoints.capabilityitem
      .getAllCapabilityItemsByItemId({ id: itemId })
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  }

  //HANDLE MODAL
  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

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
            <li className="breadcrumb-item">
              <Link
                to={`/environment/${this.state.environmentName}/strategyItem`}
              >
                Strategy Item
              </Link>
            </li>
          </ol>
        </nav>
        <MaterialTable
          title="Strategy Items"
          actions={[
            {
              icon: "add",
              tooltip: "Add Strategy Item",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/strategyitem/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "itemId" },
            { title: "Name", field: "strategyItemName" },
            { title: "Strategy", field: "strategy.strategyName" },
            { title: "Start", field: "strategy.timeFrameStart" },
            { title: "End", field: "strategy.timeFrameEnd" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.itemId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.itemId)}
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
          data={this.state.strategyItems}
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
                          {capabilityItem.capability.capabilityName}
                        </div>
                        <div
                          className="card-body text-center"
                          style={{ padding: 5 }}
                        >
                          <button
                            className="btn btn-danger"
                            onClick={() =>
                              this.unlinkCapability(
                                capabilityItem.capability.capabilityId
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
            this.setState({ itemId: rowData.itemId });
            this.capabilityTable(rowData.itemId);
            togglePanel();
          }}
        />
        <Modal show={this.state.showModal} onHide={() => this.handleModal()}>
          <Modal.Header closeButton>
            {this.state.itemId}. Add Capability
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.handleSubmit(this.state.itemId)}>
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

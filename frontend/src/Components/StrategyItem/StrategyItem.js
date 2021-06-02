import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";
import { Modal } from "react-bootstrap";
import Select from "react-select";
import toast from "react-hot-toast";

export default class StrategyItem extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      strategyItems: [],
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
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategyitem/`)
      .then((response) => {
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/capability/`)
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

  edit(strategyItemId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/strategyitem/${strategyItemId}`
    );
  }

  fetchDeleteStrategyItems = async (itemId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/strategyitem/${itemId}`)
      .then((response) => toast.success("Successfully Deleted Strategy Item"))
      .catch((error) => toast.error("Could not Delete Strategy Item"));
    //REFRESH Strategy Items
    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategyitem/`)
      .then((response) => {
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Strategy Items");
      });
  };

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  handleSubmit = (itemId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("itemId", itemId);
    formData.append("capabilityId", this.state.capabilityId);
    formData.append("strategicImportance", this.state.strategicImportance);
    await axios
      .post(`${process.env.REACT_APP_API_URL}/capabilityitem/`, formData)
      .then(toast.success("Capability Successfully Added"))
      .catch((error) => toast.error("Could not add Capability"));

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capabilityitem/all-capabilityitems-by-strategyitemid/${itemId}/`
      )
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  };

  delete = async (itemId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this strategyItem?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteStrategyItems(itemId);
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

  async capabilityTable(itemId) {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capabilityitem/all-capabilityitems-by-strategyitemid/${itemId}/`
      )
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  }

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

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
            <li className='breadcrumb-item'>
              <Link
                to={`/environment/${this.state.environmentName}/strategyItem`}
              >
                Strategy Item
              </Link>
            </li>
          </ol>
        </nav>
        <MaterialTable
          title='Strategy Items'
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
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.itemId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.itemId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={() => this.handleModal()}
                      className='bi bi-chat-square'
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
                <div className='card-deck' style={{ padding: 10, margin: 5 }}>
                  {this.state.capabilityItems.map((capabilityItem) => {
                    return (
                      <div
                        className='card'
                        style={{
                          margin: 3,
                          maxWidth: 100,
                          backgroundColor: "#ff754f65",
                        }}
                      >
                        <div className='card-body text-center text-uppercase'>
                          {capabilityItem.capability.capabilityName}
                        </div>
                      </div>
                    );
                  })}
                </div>
                <Modal show={this.state.showModal}>
                  <Modal.Header>Add Capability</Modal.Header>
                  <Modal.Body>
                    <form onSubmit={this.handleSubmit(rowData.itemId)}>
                      <label htmlFor='capabilityId'>Capability</label>
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
                        placeholder='Optional'
                      />
                      <label htmlFor='strategicImportance'>Importance</label>
                      <select
                        className='form-control'
                        name='strategicImportance'
                        id='strategicImportance'
                        placeholder='Add Importance'
                        value={this.state.strategicImportance}
                        onChange={this.handleInputChange}
                      >
                        <option value='LOWEST'>Lowest</option>
                        <option value='MEDIUM'>Medium</option>
                        <option value='HIGH'>High</option>
                        <option value='HIGHEST'>Highest</option>
                      </select>
                      <button className='btn btn-primary' type='sumbit'>
                        SUBMIT
                      </button>
                    </form>
                  </Modal.Body>
                  <Modal.Footer>
                    <button
                      type='button'
                      className='btn btn-secondary'
                      onClick={() => this.handleModal()}
                    >
                      Close Modal
                    </button>
                  </Modal.Footer>
                </Modal>
              </div>
            );
          }}
          onRowClick={(event, rowData, togglePanel) => {
            this.capabilityTable(rowData.itemId);
            togglePanel();
          }}
        />
      </div>
    );
  }
}

import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import "./GeneralTable.css";
import axios from "axios";
import { Modal } from "react-bootstrap";
import Select from "react-select";

import toast from "react-hot-toast";

export default class Capability extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      capabilities: [],
      strategyItems: [],
      itemId: 0,
      strategicImportance: "",
      capabilityItems: [],
      showItemModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  handleSubmit = (capabilityId) => async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("itemId", this.state.itemId);
    formData.append("capabilityId", capabilityId);
    formData.append("strategicImportance", this.state.strategicImportance);
    await axios
      .post(`${process.env.REACT_APP_API_URL}/capabilityitem/`, formData)
      .then(toast.success("Item Successfully Added"))
      .catch((error) => toast.error("Could not add Item"));

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capabilityitem/all-capabilityitems-by-capabilityid/${capabilityId}/`
      )
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
      });
  };

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

    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategyitem/`)
      .then((response) => {
        response.data.forEach((item) => {
          console.log(item);
          item.label = item.strategyItemName;
          item.value = item.itemId;
        });
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        console.log(error);
        toast.error("Could Not Find Strategy Items");
      });
  }

  edit(capabilityId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/capability/${capabilityId}`
    );
  }

  async strategyItemTable(capabilityId) {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capabilityitem/all-capabilityitems-by-capabilityid/${capabilityId}/`
      )
      .then((response) => {
        this.setState({ capabilityItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
      });
  }

  handleItemModal() {
    this.setState({ showItemModal: !this.state.showItemModal });
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
                title: "Actions",
                name: "actions",
                render: (rowData) => (
                  <div>
                    <button className='btn'>
                      <i
                        onClick={this.delete.bind(this, rowData.capabilityId)}
                        className='bi bi-trash'
                      ></i>
                    </button>
                    <button className='btn'>
                      <i
                        onClick={this.edit.bind(this, rowData.capabilityId)}
                        className='bi bi-pencil'
                      ></i>
                    </button>
                    <button className='btn'>
                      <i
                        onClick={() => this.handleItemModal()}
                        className='bi bi-app-indicator'
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
                  <div className='card-deck' style={{ padding: 10 }}>
                    {this.state.capabilityItems.map((capabilityItem) => {
                      return (
                        <div
                          className='card'
                          style={{ margin: 10, padding: 10 }}
                        >
                          <div className='card-header text-center text-uppercase'>
                            {capabilityItem.strategyItem.strategyItemName}
                          </div>
                          <div className='card-body'>
                            {capabilityItem.strategyItem.description}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                  <Modal show={this.state.showItemModal}>
                    <Modal.Header>
                      {rowData.capabilityId}. Add Items
                    </Modal.Header>
                    <Modal.Body>
                      <form onSubmit={this.handleSubmit(rowData.capabilityId)}>
                        <label htmlFor='itemId'>Strategy Items</label>
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
                        onClick={() => this.handleItemModal()}
                      >
                        Close Modal
                      </button>
                    </Modal.Footer>
                  </Modal>
                </div>
              );
            }}
            onRowClick={(event, rowData, togglePanel) => {
              this.strategyItemTable(rowData.capabilityId);

              togglePanel();
            }}
            options={{
              showTitle: false,
            }}
          />
        </div>
      </div>
    );
  }
}

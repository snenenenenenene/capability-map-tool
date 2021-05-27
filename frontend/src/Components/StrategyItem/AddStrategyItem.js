import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";
import Select from "react-select";

export default class StrategyItem extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      selectedCapabilities: [],
      capabilities: [],
      strategies: [],
      environmentName: this.props.match.params.name,
      itemId: "",
      environmentId: "",
      capabilityId: "",
      strategyItemName: "",
      strategyItemDescription: "",
      strategicImportance: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("strategyId", this.state.strategyId);
    formData.append("strategyItemName", this.state.strategyItemName);
    formData.append("description", this.state.strategyItemDescription);
    await axios
      .post(`${process.env.REACT_APP_API_URL}/strategyitem/`, formData)
      .then((response) => {
        toast.success("Strategy Item Added Successfully!");
        this.setState({ itemId: response.data.itemId });
      })
      .catch((error) => toast.error("Could not Add Strategy Item"));
    if (this.state.selectedCapabilities.length !== 0) {
      const formData = new FormData();
      formData.append("itemId", this.state.itemId);
      formData.append("capabilityId", this.state.capabilityId);
      formData.append("strategicImportance", this.state.strategicImportance);
      await axios
        .post(`${process.env.REACT_APP_API_URL}/capabilityitem/`, formData)
        .then((response) =>
          toast.success("Capability Item Added Successfully!")
        )
        .catch((error) => toast.error("Could not Add Capability Item"));
    }
    this.props.history.push(
      `/environment/${this.state.environmentName}/strategyitem`
    );
  };

  handleChange = (selectedOption) => {
    this.setState({ selectedCapabilities: selectedOption });
  };

  async componentDidMount() {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) => {
        this.setState({ environmentId: response.data.environmentId });
      })
      .catch((error) => {
        console.log(error);
        this.props.history.push("/notfounderror");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`
      )
      .then((response) => {
        response.data.forEach((capability) => {
          capability.label = capability.capabilityName;
          capability.value = capability.capabilityId;
        });
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not load Capabilities");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategy/`)
      .then((response) => this.setState({ strategies: response.data }))
      .catch((error) => {
        toast.error("Could not load Strategies");
      });
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  capabilityListRows() {
    return this.state.capabilities.map((capability) => {
      return (
        <option key={capability.capabilityId} value={capability.capabilityId}>
          {capability.capabilityName}
        </option>
      );
    });
  }

  onSelect(selectedList, selectedItem) {
    console.log(selectedItem);
    console.log(selectedList);
  }

  onRemove(selectedList, removedItem) {
    console.log(removedItem);
    console.log(selectedList);
  }

  strategyListRows() {
    return this.state.strategies.map((strategy) => {
      return (
        <option key={strategy.strategyId} value={strategy.strategyId}>
          {strategy.strategyName}
        </option>
      );
    });
  }

  render() {
    return (
      <div>
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
                to={`/environment/${this.state.environmentName}/strategyitem`}
              >
                Strategy Item
              </Link>
            </li>
            <li className="breadcrumb-item active" aria-current="page">
              Add Strategy Item
            </li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h3>Add Strategy Item</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm-6">
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="strategyItemName">Name Strategy Item</label>
                    <input
                      type="text"
                      id="strategyItemName"
                      name="strategyItemName"
                      className="form-control"
                      placeholder="Name Strategy Item"
                      value={this.state.strategyItemName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                  <div className="form-group col-md-6">
                    <label htmlFor="strategyId">Strategy</label>
                    <select
                      className="form-control"
                      name="strategyId"
                      id="strategyId"
                      placeholder="Add Status"
                      value={this.state.strategyId}
                      onChange={this.handleInputChange}
                    >
                      <option key="-1" defaultValue="selected" hidden value={0}>
                        None
                      </option>
                      {this.strategyListRows()}
                    </select>
                  </div>
                </div>
                <div className="form-row"></div>
                <div className="form-group">
                  <label htmlFor="strategyItemDescription">Description</label>
                  <textarea
                    type="text"
                    id="strategyItemDescription"
                    name="strategyItemDescription"
                    className="form-control"
                    rows="5"
                    placeholder="Description"
                    value={this.state.strategyItemDescription}
                    onChange={this.handleInputChange}
                  />
                </div>
              </div>
              <div className="col-sm-6">
                <div className="form-group col-md">
                  <label htmlFor="strategicImportance">Importance</label>
                  <select
                    className="form-control"
                    name="strategicImportance"
                    id="strategicImportance"
                    placeholder="Add Importance"
                    value={this.state.strategicImportance}
                    onChange={this.handleInputChange}
                  >
                    <option defaultValue="selected" hidden value="ONE">
                      Optional
                    </option>
                    <option value="ONE">ONE</option>
                    <option value="TWO">TWO</option>
                    <option value="THREE">THREE</option>
                  </select>
                </div>
                <div className="form-group col-md">
                  <label htmlFor="capabilityId">Capability</label>
                  <Select
                    options={this.state.capabilities}
                    isMulti
                    closeMenuOnSelect={false}
                    onChange={this.handleChange}
                    placeholder="Optional"
                  />
                </div>
              </div>
            </div>
            <button
              className="btn btn-primary"
              type="button"
              onClick={this.handleSubmit}
            >
              Submit
            </button>
          </form>
        </div>
      </div>
    );
  }
}

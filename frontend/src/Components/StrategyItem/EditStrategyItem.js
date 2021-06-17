import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import API from "../../Services/API";

export default class EditStrategy extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),

      environments: [],
      selectedCapabilities: [],
      capabilities: [],
      strategies: [],
      environmentName: this.props.match.params.name,
      itemId: this.props.match.params.id,
      environmentId: "",
      capabilityId: "",
      strategyItemName: "",
      strategyItemDescription: "",
      strategicImportance: "",
      strategyId: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  //SUBMIT STRATEGY ITEM
  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("strategyId", this.state.strategyId);
    formData.append("itemId", this.state.itemId);
    formData.append("strategyItemName", this.state.strategyItemName);
    formData.append("description", this.state.strategyItemDescription);
    await this.state.api.endpoints.strategyitem
      .update(formData, this.state.strategyItemId)
      .then((response) => {
        toast.success("Strategy Item Edited Successfully!");
        this.props.history.push(
          `/environment/${this.state.environmentName}/strategyitem`
        );
      })
      .catch((error) => toast.error("Could not Edit Strategy Item"));
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "status" });
    this.state.api.createEntity({ name: "strategy" });
    this.state.api.createEntity({ name: "capabiliy" });

    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.capability
      .getAllCapabilitiesByEnvironmentId({ id: this.state.environmentId })
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

    await this.state.api.endpoints.strategy
      .getAll()
      .then((response) => this.setState({ strategies: response.data }))
      .catch((error) => {
        toast.error("Could not load Strategies");
      });

    await this.state.api.endpoints.strategyitem
      .getOne({ id: this.state.strategyItemId })
      .then((response) =>
        this.setState({
          capabilityId: response.data.capabilityId,
          strategyItemName: response.data.strategyItemName,
          strategyItemDescription: response.data.description,
          strategicImportance: response.data.strategicImportance,
          strategyId: response.data.strategy.strategyId,
        })
      )
      .catch((error) => {
        toast.error("Could not load Strategies");
      });
  }

  //HANDLE INPUT CHANGE
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }
  //FETCH STRATEGIES AND INSERT THEM INTO HTML SELECT
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
                to={`/environment/${this.state.environmentName}/strategyitem`}
              >
                Strategy Item
              </Link>
            </li>
            <li className="breadcrumb-item active" aria-current="page">
              {this.state.itemId}
            </li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h3>Edit Strategy Item</h3>
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

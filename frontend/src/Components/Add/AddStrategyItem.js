import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import axios from 'axios';
import toast from 'react-hot-toast';

export default class StrategyItem extends Component {
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            capabilities: [],
            strategies: [],
            environmentName: this.props.match.params.name,
            environmentId:'',
            capabilityId: '',
            strategyItemName: '',
            strategyItemDescription: '',
            strategicImportance: ''
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this)
    }

    handleSubmit = async e => {
        e.preventDefault();
        const formData = new FormData()
        formData.append('environmentName', this.state.environmentName)
        formData.append('environmentId', this.state.environmentId)
        formData.append('capabilityId', this.state.capabilityId)
        formData.append('strategyItemName', this.state.strategyItemName)
        formData.append('strategyItemDescription', this.state.strategyItemDescription)
        formData.append('strategicImportance', this.state.strategicImportance)
        await axios.post(`${process.env.REACT_APP_API_URL}/capability/`, formData)
        .then(response => toast.success("Strategy Item Added Successfully!"))
        .catch(error => toast.error("Could not Add Strategy Item"))
        this.props.history.push(`/environment/${this.state.environmentName}/strategyitem`)
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`)
        .then(response => {
            this.setState({environmentId: response.data.environmentId})
        })
        .catch(error => {
            console.log(error)
            this.props.history.push('/notfounderror')
        })

        await axios.get(`${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`)
        .then(response => this.setState({capabilities: response.data}))
        .catch(error => {
            toast.error("Could not load Capabilities")
        })

        await axios.get(`${process.env.REACT_APP_API_URL}/strategy/`)
        .then(response => this.setState({strategies: response.data}))
        .catch(error => {
            toast.error("Could not load Strategies")
        })
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    capabilityListRows() {
        return this.state.capabilities.map((capability) => {
            return <option key={capability.capabilityId} value={capability.capabilityId}>{capability.capabilityName}</option>
        })
    }

    strategyListRows() {
        return this.state.strategies.map((strategy) => {
            return <option key={strategy.strategyId} value={strategy.strategyId}>{strategy.strategyName}</option>
        })
    }

    render() {
        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}`}>{this.state.environmentName}</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}/strategyitem`}>Strategy Item</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add Strategy Item</li>
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
                                <input type="text" id="strategyItemName" name="strategyItemName" className="form-control" placeholder="Name Strategy Item"
                                       value={this.state.strategyItemName} onChange={this.handleInputChange}/>
                                </div>
                                <div className="form-group col-md-6">
                                    <label htmlFor="strategyId">Strategy</label>
                                <select className="form-control" name="strategyId" id="strategyId" placeholder="Add Status"
                                        value={this.state.strategyId} onChange={this.handleInputChange}>
                                    <option key="-1" defaultValue="selected" value={0}>None</option>
                                    {this.strategyListRows()}
                                </select>
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="capabilityId">Capability</label>
                                <select className="form-control" name="capabilityId" id="capabilityId" placeholder="Add Capability"
                                        value={this.state.capabilityId} onChange={this.handleInputChange}>
                                    <option key="-1" defaultValue="selected" value={0}>None</option>
                                    {this.capabilityListRows()}
                                </select>
                                </div>
                                <div className="form-group col-md-6">
                                    <label htmlFor="strategicImportance">Importance</label>
                                <select className="form-control" name="strategicImportance" id="strategicImportance" placeholder="Add Importance"
                                        value={this.state.strategicImportance} onChange={this.handleInputChange}>
                                    <option defaultValue="selected" value="ONE">ONE</option>
                                    <option value="TWO">TWO</option>
                                    <option value="THREE">THREE</option>
                                </select>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="strategyItemDescription">Description</label>
                                <textarea type="text" id="strategyItemDescription" name="strategyItemDescription" className="form-control" rows="5" placeholder="Description"
                                          value={this.state.strategyItemDescription} onChange={this.handleInputChange}/>
                            </div>
                        </div>
                        <div className="col-sm-6">
                        </div>
                    </div>
                    <button className="btn btn-primary" type="button" onClick={this.handleSubmit}>Submit</button>
                </form>
            </div>
        </div>
        )
    }
}
import React, {Component} from 'react';
import {Link} from 'react-router-dom';

export default class AddStatus extends Component {
    constructor(props) {
        super(props);
        this.state = {
            statuses: [],
            environments: [],
            environmentName: this.props.match.params.name,
            validityPeriod: new Date(),
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this)
        this.handleDateChange = this.handleDateChange.bind(this)
    }

    handleSubmit = async e => {
        e.preventDefault();
        const formData = new FormData()
        formData.append('validityPeriod', this.state.validityPeriod)
        await fetch(`http://localhost:8080/status/add`,{
            method: "POST",
            body: formData
        }).then(function (res) {
            if (res.ok) {
                console.log("Status added");
            } else if (res.status === 401) {
                console.log("Oops,, Something went wrong");
            }})
    }

    async componentDidMount() {
        const statusResponse = await fetch(`http://localhost:8080/status/all`);
        const statusData = await statusResponse.json();
        this.setState({statuses: statusData});
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    handleDateChange(event) {
        this.setState({[event.target.name] : event.target.value.toLocaleString()})
        console.log(this.state.validityPeriod)
    }

    statusListRows() {
        return this.state.statuses.map((status) => {
            console.log(status.statusId + " " + status.validityPeriod + " Days")
            // return <option value={status.statusId}>{status.validityPeriod} Days</option>
            return <option value={status.statusId}>{status.validityPeriod}</option>
        })
    }

    render() {
        const environmentName = this.props.match.params.name;
        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}>{environmentName}</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add Status</li>
                    </ol>
                </nav>
                <div className="jumbotron">
                    <h3>Add Status</h3>
                    <form onSubmit={this.handleSubmit}>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="form-row">
                                    <div className="form-group col-md-6">
                                        <label htmlFor="validityPeriod">Validity Period</label>
                                        <input type="date" id="validityPeriod" name="validityPeriod" className="form-control" placeholder="End Date"
                                               value={this.state.validityPeriod} onChange={this.handleDateChange}/>
                                    </div>
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